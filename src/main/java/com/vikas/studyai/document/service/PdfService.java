package com.vikas.studyai.document.service;

import com.vikas.studyai.document.model.PageContent;
import java.nio.file.Path;
import java.util.List;

public interface PdfService {
    List<PageContent> extract(Path file);
}
