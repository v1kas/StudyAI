CREATE TABLE study_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_id UUID NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
    started_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMPTZ,
    CONSTRAINT chk_study_sessions_dates CHECK (completed_at IS NULL OR completed_at >= started_at)
);

CREATE INDEX idx_study_sessions_document_id ON study_sessions(document_id);
