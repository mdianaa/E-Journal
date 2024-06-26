package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.HeadmasterDtoResponse;
import org.example.ejournal.services.HeadmasterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/headmasters")
public class HeadmasterController {

    private final HeadmasterService headmasterService;

    public HeadmasterController(HeadmasterService headmasterService) {
        this.headmasterService = headmasterService;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> showCreateHeadmasterPage() {
        return ResponseEntity.ok("create headmaster");
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HeadmasterDtoResponse> createHeadmaster(@Valid @RequestBody HeadmasterDtoRequest headmasterDto,
                                                                  @Valid @RequestBody SchoolDtoRequest schoolDto,
                                                                  @Valid @RequestBody UserRegisterDtoRequest userRegisterDtoRequest) {
        HeadmasterDtoResponse createdHeadmasterDto = headmasterService.createHeadmaster(headmasterDto, schoolDto, userRegisterDtoRequest);
        return new ResponseEntity<>(createdHeadmasterDto, HttpStatus.CREATED);
    }

    @GetMapping("/view/{headmasterId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'PARENT', 'HEADMASTER')")
    public ResponseEntity<HeadmasterDtoResponse> viewHeadmaster(@PathVariable long headmasterId) {
        HeadmasterDtoResponse headmaster = headmasterService.viewHeadmaster(headmasterId);
        return headmaster != null ? ResponseEntity.ok(headmaster) : ResponseEntity.notFound().build();
    }

    @GetMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> showEditHeadmasterPage() {
        return ResponseEntity.ok("edit headmaster");
    }

    @PutMapping("/edit/{headmasterId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HeadmasterDtoResponse> editHeadmaster(@PathVariable long headmasterId,
                                                                @Valid @RequestBody HeadmasterDtoRequest headmasterDto) {
        HeadmasterDtoResponse editedHeadmasterDto = headmasterService.editHeadmaster(headmasterId, headmasterDto);
        return editedHeadmasterDto != null ? ResponseEntity.ok(editedHeadmasterDto) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{headmasterId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteHeadmaster(@PathVariable long headmasterId) {
        headmasterService.deleteHeadmaster(headmasterId);
        return ResponseEntity.noContent().build();
    }
}
