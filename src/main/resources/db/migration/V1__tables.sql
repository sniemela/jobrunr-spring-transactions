CREATE TABLE post (
    id bigint PRIMARY KEY,
    content text NOT NULL,
    version bigint NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);

CREATE INDEX idx_post_id_version ON post(id, version);

CREATE TABLE comment (
    id bigint PRIMARY KEY,
    post_id bigint NOT NULL,
    content text NOT NULL,
    version bigint NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);

CREATE INDEX idx_comment_post_id ON comment(post_id);
CREATE INDEX idx_comment_id_version ON comment(id, version);