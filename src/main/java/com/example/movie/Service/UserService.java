package com.example.movie.Service;

import com.example.movie.Entity.User;
import com.example.movie.Repository.RoleRepo;
import com.example.movie.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private RoleRepo roleRepository;

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void save(User user) {
        userRepository.save(user);
        //lấy về userId của user vừa được thêm vào
        Long userId = userRepository.getUserIdByUsername(user.getUsername());
        // lấy về roleId của role có tên User
        Long roleId = roleRepository.getRoleIdByName("USER");

        if (roleId != 0 && userId != 0) {
            //thêm role của user vào bảng user_role
            userRepository.addRoleToUser(userId, roleId);
        }
    }

    public User addSUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        //goi repository ==> deleteById
        userRepository.deleteById(id);
    }

    public User updateUser(User user) {
        // Kiểm tra xem người dùng có tồn tại trong cơ sở dữ liệu hay không
        Optional<User> existingUserOptional = userRepository.findById(user.getUserId());
        if (existingUserOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        // Lưu thông tin người dùng đã được cập nhật
        User existingUser = existingUserOptional.get();
        existingUser.setEmail(user.getEmail());
        existingUser.setFullName(user.getFullName());
        existingUser.setPassword(user.getPassword());
        existingUser.setUsername(user.getUsername());

        // Lưu cập nhật vào cơ sở dữ liệu
        return userRepository.save(existingUser);
    }

    public List<User> getAllUser(Integer pageNo,
                                 Integer pageSize,
                                 String sortBy){
        return userRepository.getAllUser(pageNo, pageSize, sortBy);
    }

    public List<User> searchUser(String keyword){
        return userRepository.searchUser(keyword);
    }

    public String getDiscountPercentageByUsername(String username){
        String discountPercentage = userRepository.getDiscountPercentageByUsername(username);
        return discountPercentage;
    }

    public int getRewardPoints(String username){
        return userRepository.getRewardPoints(username);
    }

}
