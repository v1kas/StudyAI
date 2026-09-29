CREATE TABLE questions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_id UUID NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    question_type VARCHAR(20) NOT NULL
        CHECK (question_type IN ('CONCEPTUAL', 'DEFINITION', 'EXPLANATION', 'COMPARISON', 'APPLICATION')),
    difficulty VARCHAR(10) NOT NULL
        CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD')),
    expected_answer TEXT NOT NULL,
    source_chunks JSONB NOT NULL DEFAULT '[]'::jsonb,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_questions_document_id ON questions(document_id);
