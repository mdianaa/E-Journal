package org.example.ejournal.services;

import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.BadNoteDtoResponse;

import java.util.List;

public interface BadNoteService {

    BadNoteDtoResponse createBadNote(BadNoteDtoRequest badNoteDtoRequest, long studentId);

    List<BadNoteDtoResponse> getBadNotesForStudent(long studentId);

    List<BadNoteDtoResponse> showAllBadNotesForStudentAsStudent();

    List<BadNoteDtoResponse> showAllBadNotesForStudentAsParent(long studentId);

    List<BadNoteDtoResponse> showAllBadNotesForStudentAsTeacher(long studentId);

    List<BadNoteDtoResponse> showAllBadNotesForStudentAsHeadmaster(long studentId);

    void deleteBadNote(long badNoteId);
}
