package com.utility.billing.security;
 
import com.utility.billing.billing.BillRepository;
import com.utility.billing.customer.CustomerRepository;
import com.utility.billing.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
 
@Service("securityService")
public class SecurityService {
 
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
 
    public SecurityService(BillRepository billRepository, UserRepository userRepository, CustomerRepository customerRepository) {
        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }
 
    // Retrieves the email of the currently authenticated user from the SecurityContext
    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            return ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }
        return null;
    }
 
    // Checks if the currently authenticated user is the owner of the specified bill
    public boolean isBillOwner(Long billId) {
        String userEmail = getCurrentUserEmail();
        if (userEmail == null) return false;
 
        return billRepository.findById(billId)
                .map(bill -> bill.getCustomer().getUser() != null && bill.getCustomer().getUser().getEmail().equals(userEmail))
                .orElse(false);
    }
 
    // Checks if the currently authenticated user is the owner of the specified user profile
    public boolean isUserOwner(Long userId) {
        String userEmail = getCurrentUserEmail();
        if (userEmail == null) return false;
 
        return userRepository.findById(userId)
                .map(user -> user.getEmail().equals(userEmail))
                .orElse(false);
    }
 
    // Checks if the currently authenticated user is the owner of the specified customer record
    public boolean isCustomerOwner(Long customerId) {
        String userEmail = getCurrentUserEmail();
        if (userEmail == null) return false;
 
        return customerRepository.findById(customerId)
                .map(customer -> customer.getUser() != null && customer.getUser().getEmail().equals(userEmail))
                .orElse(false);
    }
}