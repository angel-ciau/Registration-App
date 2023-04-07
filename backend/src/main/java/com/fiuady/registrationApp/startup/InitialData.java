package com.fiuady.registrationApp.startup;

import com.fiuady.registrationApp.entities.Group;
import com.fiuady.registrationApp.entities.Permission;
import com.fiuady.registrationApp.entities.Role;
import com.fiuady.registrationApp.entities.User;
import com.fiuady.registrationApp.repositories.GroupRepository;
import com.fiuady.registrationApp.repositories.PermissionRepository;
import com.fiuady.registrationApp.repositories.RoleRepository;
import com.fiuady.registrationApp.repositories.UserRepository;
import com.fiuady.registrationApp.utils.RandomUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class InitialData {

    @Autowired private PasswordEncoder argon2PasswordEncoder;
    @Autowired private UserRepository userRepo;
    @Autowired private RoleRepository roleRepo;
    @Autowired private PermissionRepository permRepo;
    @Autowired private GroupRepository groupRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void createInitialData() {
        createPermissions();
        createRoles();
        createUsers();
        createGroups();
    }

    private void createGroups() {
        User user1 = userRepo.findById(1L).get();
        User user2 = userRepo.findById(2L).get();

        Group group1 = new Group();
        group1.setName("User's 1 group");
        group1.setParticipants(Set.of(user2));
        group1.setOwner(user1);

        groupRepository.save(group1);

        Group group2 = new Group();
        group2.setName("User's 1 group 2");
        group2.setOwner(user1);

        groupRepository.save(group2);
    }

    private void createUsers() {
        Set<Role> roles = roleRepo.findAll().stream().collect(Collectors.toSet());

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername(String.format("user_%s", i + 1));
            user.setPassword(argon2PasswordEncoder.encode("password"));
            user.setRoles(RandomUtils.getRandomElementsFromSet(roles, 5));

            userRepo.save(user);
        }
    }

    private void createRoles() {
        Set<Permission> permissions = permRepo.findAll().stream().collect(Collectors.toSet());
        Set<Role> roles = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            Role role = new Role();
            role.setName(String.format("role_%s", i + 1));
            role.setPermissions(RandomUtils.getRandomElementsFromSet(permissions, 5));

            roles.add(role);
        }

        roleRepo.saveAll(roles);
    }

    private void createPermissions() {
        Set<Permission> permissions = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            Permission permission = new Permission();
            permission.setName(String.format("permission_%s", i + 1));

            permissions.add(permission);
        }

        permRepo.saveAll(permissions);
    }
}
