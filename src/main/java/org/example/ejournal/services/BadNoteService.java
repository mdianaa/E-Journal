package org.example.ejournal.services;

import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.BadNoteDtoResponse;

public interface BadNoteService {

    BadNoteDtoResponse createBadNote(BadNoteDtoRequest badNoteDtoRequest, long studentId);

    void deleteBadNote(long badNoteId);
}
