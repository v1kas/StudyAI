CREATE TABLE evaluations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    answer_id UUID NOT NULL UNIQUE REFERENCES answers(id) ON DELETE CASCADE,
    overall_score NUMERIC(5, 2) NOT NULL CHECK (overall_score BETWEEN 0 AND 100),
    correctness_score NUMERIC(5, 2) NOT NULL CHECK (correctness_score BETWEEN 0 AND 100),
    completeness_score NUMERIC(5, 2) NOT NULL CHECK (completeness_score BETWEEN 0 AND 100),
    relevance_score NUMERIC(5, 2) NOT NULL CHECK (relevance_score BETWEEN 0 AND 100),
    clarity_score NUMERIC(5, 2) NOT NULL CHECK (clarity_score BETWEEN 0 AND 100),
    feedback TEXT NOT NULL,
    correct_answer TEXT NOT NULL,
    missing_concepts JSONB NOT NULL DEFAULT '[]'::jsonb,
    incorrect_concepts JSONB NOT NULL DEFAULT '[]'::jsonb,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
