package com.springbank.user.query.api.controllers;

import com.springbank.user.query.api.dto.UserLookupResponse;
import com.springbank.user.query.api.queries.FindAllUsersQuery;
import com.springbank.user.query.api.queries.FindUserByIdQuery;
import com.springbank.user.query.api.queries.SearchUsersQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/userLookup")
@RequiredArgsConstructor
public class UserLookupController {

    private final QueryGateway queryGateway;

    @GetMapping
    public ResponseEntity<UserLookupResponse> getAllUsers() {

        try {
            var query = new FindAllUsersQuery();
            var response = queryGateway.query(query,
                    ResponseTypes.instanceOf(UserLookupResponse.class)).join();

            if (response == null || response.getUsers() == null) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete get all users request";

            return new ResponseEntity<>(new UserLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<UserLookupResponse> getUserById(@PathVariable String id) {

        try {
            var query = new FindUserByIdQuery(id);
            var response = queryGateway.query(query,
                    ResponseTypes.instanceOf(UserLookupResponse.class)).join();

            if (response == null || response.getUsers() == null) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete get user by ID request";

            return new ResponseEntity<>(new UserLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byFilter/{filter}")
    public ResponseEntity<UserLookupResponse> searchUserByFilter(@PathVariable String filter) {

        try {
            var query = new SearchUsersQuery(filter);
            var response = queryGateway.query(query,
                    ResponseTypes.instanceOf(UserLookupResponse.class)).join();

            if (response == null || response.getUsers() == null) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete user search request";

            return new ResponseEntity<>(new UserLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
