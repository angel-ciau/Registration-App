package com.fiuady.registrationApp.controllers;

import com.fiuady.registrationApp.entities.Group;
import com.fiuady.registrationApp.entities.User;
import com.fiuady.registrationApp.services.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupsController {

    @Autowired private GroupService groupService;

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        return new ResponseEntity<>(groupService.createNewGroup(group), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Group>> getLoggedInUserGroups() {
        List<Group> loggedInUserGroups = groupService.getAllForLoggedInUser();

        for (Group group : loggedInUserGroups) {
            group.getOwner().setPassword(null);
            group.getOwner().setRoles(null);
            for (User user : group.getParticipants()) {
                user.setPassword(null);
                user.setRoles(null);
            }
        }

        return new ResponseEntity<>(loggedInUserGroups, HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Object> deleteGroup(@PathVariable("groupId") Long groupId) {
        groupService.deleteById(groupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // @GetMapping("/{groupId}")
    // public ResponseEntity<Group> getById(@PathVariable Long groupId) {
    // Group group = groupService.getById(groupId);

    // group.getOwner().setPassword(null);
    // group.getOwner().setRoles(null);

    // return new ResponseEntity<>(group, HttpStatus.OK);
    // }
}
