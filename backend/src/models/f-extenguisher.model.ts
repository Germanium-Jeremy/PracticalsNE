export interface FireExtinguisher {
    id: number;
    user_id: number | null;
    created_at: string;
    bought_at: string | null;
    status: string;
    returned_at: string | null;
    expires_at: string;
    user_notified: number;
    admin_notified: number;
}