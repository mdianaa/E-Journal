package org.example.ejournal.web;

import jakarta.validation.Valid;
import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.HeadmasterDtoResponse;
import org.example.ejournal.services.HeadmasterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/headmasters")
public class HeadmasterController {

    private final HeadmasterService headmasterService;

    public HeadmasterController(HeadmasterService headmasterService) {
        this.headmasterService = headmasterService;
    }

    @PostMapping("/create")
    public ResponseEntity<HeadmasterDtoRequest> createHeadmaster(@Valid @RequestBody HeadmasterDtoRequest headmasterDto,
                                                                 @Valid @RequestBody SchoolDtoRequest schoolDto) {
        HeadmasterDtoRequest createdHeadmasterDto = headmasterService.createHeadmaster(headmasterDto, schoolDto);
        return new ResponseEntity<>(createdHeadmasterDto, HttpStatus.CREATED);
    }

    @GetMapping("/view/{headmasterId}")
    public ResponseEntity<HeadmasterDtoResponse> viewHeadmaster(@PathVariable long headmasterId) {
        HeadmasterDtoResponse headmaster = headmasterService.viewHeadmaster(headmasterId);
        if (headmaster != null) {
            return ResponseEntity.ok(headmaster);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/edit/{headmasterId}")
    public ResponseEntity<HeadmasterDtoRequest> editHeadmaster(@PathVariable long headmasterId,
                                                               @Valid @RequestBody HeadmasterDtoRequest headmasterDto) {
        HeadmasterDtoRequest editedHeadmasterDto = headmasterService.editHeadmaster(headmasterId, headmasterDto);
        if (editedHeadmasterDto != null) {
            return ResponseEntity.ok(editedHeadmasterDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{headmasterId}")
    public ResponseEntity<Void> deleteHeadmaster(@PathVariable long headmasterId) {
        headmasterService.deleteHeadmaster(headmasterId);
        return ResponseEntity.noContent().build();
    }
}
