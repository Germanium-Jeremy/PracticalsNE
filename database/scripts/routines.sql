-- 1. Trigger after bill generation: Insert notification record
CREATE OR REPLACE FUNCTION after_bill_generation_trigger()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO notifications (customer_id, bill_id, message, status, created_at)
    VALUES (
        NEW.customer_id, 
        NEW.id, 
        'Dear Customer, Your ' || NEW.billing_month || '/' || NEW.billing_year || ' utility bill of ' || NEW.total_amount || ' FRW has been successfully processed.', 
        'PENDING', 
        NOW()
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_after_bill_generation
AFTER INSERT ON bills
FOR EACH ROW
EXECUTE FUNCTION after_bill_generation_trigger();


-- 2. Trigger after full payment: Update bill status and Create payment notification
CREATE OR REPLACE FUNCTION after_payment_trigger()
RETURNS TRIGGER AS $$
DECLARE
    v_balance DOUBLE PRECISION;
    v_customer_id BIGINT;
    v_bill_number VARCHAR;
BEGIN
    -- Get bill info
    SELECT balance, customer_id, bill_number INTO v_balance, v_customer_id, v_bill_number 
    FROM bills WHERE id = NEW.bill_id;

    -- Update bill status if balance is zero
    IF v_balance <= 0 THEN
        UPDATE bills SET status = 'PAID' WHERE id = NEW.bill_id;
        
        -- Create notification
        INSERT INTO notifications (customer_id, bill_id, message, status, created_at)
        VALUES (
            v_customer_id, 
            NEW.bill_id, 
            'Dear Customer, Your payment of ' || NEW.amount_paid || ' FRW for bill ' || v_bill_number || ' has been successfully received. Your bill is now fully PAID.', 
            'PENDING', 
            NOW()
        );
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_after_payment
AFTER INSERT ON payments
FOR EACH ROW
EXECUTE FUNCTION after_payment_trigger();


-- 3. Stored procedure: Generate monthly bills for all active meters that have readings but no bill yet
CREATE OR REPLACE PROCEDURE generate_monthly_bills(p_month INTEGER, p_year INTEGER)
LANGUAGE plpgsql
AS $$
DECLARE
    r_meter RECORD;
    v_reading RECORD;
    v_tariff RECORD;
    v_consumption DOUBLE PRECISION;
    v_tariff_amount DOUBLE PRECISION;
    v_tax_amount DOUBLE PRECISION;
    v_total_amount DOUBLE PRECISION;
BEGIN
    FOR r_meter IN 
        SELECT m.id, m.customer_id, m.meter_type 
        FROM meters m 
        JOIN customers c ON m.customer_id = c.id 
        WHERE m.status = 'ACTIVE' AND c.status = 'ACTIVE'
    LOOP
        -- Check if bill already exists
        IF NOT EXISTS (SELECT 1 FROM bills WHERE meter_id = r_meter.id AND billing_month = p_month AND billing_year = p_year) THEN
            
            -- Get reading for this month/year
            SELECT * INTO v_reading FROM meter_readings 
            WHERE meter_id = r_meter.id AND billing_month = p_month AND billing_year = p_year;
            
            IF FOUND THEN
                -- Get active tariff
                SELECT * INTO v_tariff FROM tariffs 
                WHERE meter_type = r_meter.meter_type AND active = true 
                ORDER BY version DESC LIMIT 1;
                
                IF FOUND THEN
                    v_consumption := v_reading.current_reading - v_reading.previous_reading;
                    v_tariff_amount := v_consumption * v_tariff.rate;
                    v_tax_amount := v_tariff_amount * 0.18;
                    v_total_amount := v_tariff_amount + v_tax_amount;
                    
                    INSERT INTO bills (bill_number, customer_id, meter_id, billing_month, billing_year, consumption, tariff_amount, tax_amount, penalty_amount, total_amount, paid_amount, balance, status, generated_date)
                    VALUES (
                        'BILL-' || UPPER(SUBSTRING(MD5(RANDOM()::TEXT), 1, 8)),
                        r_meter.customer_id,
                        r_meter.id,
                        p_month,
                        p_year,
                        v_consumption,
                        v_tariff_amount,
                        v_tax_amount,
                        0,
                        v_total_amount,
                        0,
                        v_total_amount,
                        'PENDING',
                        NOW()
                    );
                END IF;
            END IF;
        END IF;
    END LOOP;
END;
$$;
