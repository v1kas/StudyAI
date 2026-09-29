package com.vikas.studyai.document.model;

/** Text extracted from one PDF page. Page numbers are one-based. */
public record PageContent(int pageNumber, String content) { }
