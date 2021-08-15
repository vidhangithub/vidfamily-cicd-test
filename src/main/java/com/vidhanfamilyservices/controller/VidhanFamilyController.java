package com.vidhanfamilyservices.controller;

import com.vidhanfamilyservices.model.VidhanFamily;
import com.vidhanfamilyservices.repository.VidhanFamilyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("vidhanapi/v1.0")
public class VidhanFamilyController {

    @Autowired
    private VidhanFamilyRepository vidhanFamilyRepository;

    @GetMapping("hellofromfamily")
    public String helloFromVidhan() {
        return "Hello There!!!!!";
    }

    @GetMapping("familymembers")
    public List<VidhanFamily> getAllFamilyMembers() {
        return vidhanFamilyRepository.findAll();
    }

    @GetMapping("familymember/{id}")
    public ResponseEntity<VidhanFamily> getFamilyMember(@PathVariable Long id) {
        VidhanFamily familyMember = vidhanFamilyRepository.findById(id).isPresent()
                ? vidhanFamilyRepository.findById(id).get() : null;
        if (familyMember != null) {
            return ResponseEntity.ok().body(familyMember);
        } else return ResponseEntity.notFound().build();
    }

    @PostMapping("familymember")
    public ResponseEntity<VidhanFamily> createFamilyMember(@RequestBody VidhanFamily vidhanFamily) {
        VidhanFamily createdFamilyMember = vidhanFamilyRepository.save(vidhanFamily);
        if (createdFamilyMember != null) {
            return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{/id}").buildAndExpand(createdFamilyMember.getFamilyMemberId()).toUri())
                    .body(createdFamilyMember);
        } else return ResponseEntity.badRequest().build();
    }

    @RequestMapping(value = {"familymember/{id}"},
            method = {RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<HttpStatus> methodsNotAllowed() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

}
