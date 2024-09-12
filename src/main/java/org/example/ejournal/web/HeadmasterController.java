package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.response.HeadmasterDtoResponse;
import org.example.ejournal.services.HeadmasterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<?> createHeadmaster(@Valid @RequestBody HeadmasterDtoRequest headmasterDtoRequest) {
        try{HeadmasterDtoResponse createdHeadmasterDto = headmasterService
                .createHeadmaster
                        (headmasterDtoRequest);
        return new ResponseEntity<>(createdHeadmasterDto, HttpStatus.CREATED);
        }catch (Exception e) {
	        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/view/{headmasterId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'PARENT', 'HEADMASTER')")
    public ResponseEntity<HeadmasterDtoResponse> viewHeadmaster(@PathVariable long headmasterId) {
        HeadmasterDtoResponse headmaster = headmasterService.viewHeadmaster(headmasterId);
        return headmaster != null ? ResponseEntity.ok(headmaster) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/viewAll")
    @PreAuthorize("hasAnyRole('HEADMASTER','ADMIN')")
    public ResponseEntity<?> viewAllHeadmasters(){
        try{
            Set<HeadmasterDtoResponse> headmasterDtoResponseList = headmasterService.viewAllHeadmasters();
            return ResponseEntity.ok(headmasterDtoResponseList);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/view-by-schoolId/{schoolId}")
    @PreAuthorize("hasAnyRole('HEADMASTER','ADMIN')")
    public ResponseEntity<?> viewAllHeadmasters(@PathVariable @Valid long schoolId){
        try{
            Set<HeadmasterDtoResponse> headmasterDtoResponseList = headmasterService.viewAllHeadmasters();
            return ResponseEntity.ok(headmasterDtoResponseList);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/school/{schoolId}")
    @PreAuthorize("hasAnyRole('HEADMASTER','ADMIN')")
    public ResponseEntity<?> viewAllHeadmastersBySchoolId(@PathVariable int schoolId){
        try{
            Set<HeadmasterDtoResponse> headmasterDtoResponseList = headmasterService.viewAllHeadmastersInSchool(schoolId);
            return ResponseEntity.ok(headmasterDtoResponseList);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
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
