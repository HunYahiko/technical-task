--
--    Copyright 2010-2016 the original author or authors.
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

-- // create_post_comments_table
-- Migration SQL that makes the change goes here.
CREATE SEQUENCE post_comments_seq MINVALUE 1 INCREMENT BY 1 START WITH 1 NO CYCLE as integer
/execute/

CREATE TABLE POST_COMMENTS
(
    ID      serial        not null primary key,
    REVIEW  varchar(250)  not null,
    POST_ID int,
    CONSTRAINT FK_POSTS_POST_COMMENTS FOREIGN KEY (POST_ID) REFERENCES POSTS(ID)
)
/execute/

-- //@UNDO
-- SQL to undo the change goes here.
DROP TABLE POST_COMMENTS
/execute/

DROP SEQUENCE post_comments_seq
/execute/