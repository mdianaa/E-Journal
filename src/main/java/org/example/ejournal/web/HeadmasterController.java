package org.example.ejournal.web;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.response.HeadmasterDtoResponse;
import org.example.ejournal.services.HeadmasterService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/headmaster")
public class HeadmasterController {

    private final HeadmasterService service;

    // Get headmaster of a particular school
    @GetMapping("/school/{schoolId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEADMASTER', 'STUDENT', 'TEACHER', 'PARENT')")
    public HeadmasterDtoResponse bySchool(@PathVariable long schoolId) {
        return service.viewHeadmaster(schoolId);
    }

    // Get all headmasters
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Set<HeadmasterDtoResponse> all() {
        return service.viewAllHeadmasters();
    }

    // Remove a headmaster
    @DeleteMapping("/{headmasterId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void delete(@PathVariable long headmasterId) {
        service.deleteHeadmaster(headmasterId);
    }
}
