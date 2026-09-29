package com.vikas.studyai.common.validation;

/** Marker interfaces for validation rules that vary by operation. */
public final class ValidationGroups {
    private ValidationGroups() { }

    public interface Create { }
    public interface Update { }
}
