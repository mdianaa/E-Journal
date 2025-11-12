package org.example.ejournal.services;

import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.BadNoteDtoResponse;

import java.util.Set;

public interface BadNoteService {

    BadNoteDtoResponse createBadNote(BadNoteDtoRequest badNoteDtoRequest);

    Set<BadNoteDtoResponse> viewAllBadNotesForStudent(long studentId);

    Set<BadNoteDtoResponse> viewAllBadNotesGivenByTeacher(long teacherId);

    void deleteBadNote(long badNoteId);
}
