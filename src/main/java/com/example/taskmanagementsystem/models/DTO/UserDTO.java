    package com.example.taskmanagementsystem.models.DTO;
    
    import com.example.taskmanagementsystem.models.User;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    
    import javax.persistence.Column;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class UserDTO {
        private Long id;
        private String email;
        private byte isModerator;
    
        public UserDTO(User user) {
            if (user != null) {
                setId(user.getId());
                setEmail(user.getEmail());
                setIsModerator(user.getIsModerator());
            }
        }
    }
    
    
