## Relations
### `actor_sample`
| Column | Description         | References |
| ------ | ------------------- | ---------- |
| id     | Unique ID for actor | PK         |
| name   | Actor’s name        | —          |
| gender | Actor’s gender      | —          |

### `cast_sample`
| Column   | Description            | References             |
| -------- | ---------------------- | ---------------------- |
| id       | Unique ID for cast row | PK                     |
| actor_id | Actor’s ID             | FK → `actor_sample.id` |
| movie_id | Movie ID               | FK → `movie_sample.id` |
| role_id  | Role ID                | FK → `role_type.id`    |

### `movie_sample`
|Column|Description|References|
|---|---|---|
|id|Unique ID for movie|PK|
|title|Movie title|—|
|production_year|Year movie released|—|

### `movie_info_sample`
|Column|Description|References|
|---|---|---|
|id|Unique ID for movie info|PK|
|movie_id|Movie ID|FK → `movie_sample.id`|
|info_type_id|Info type ID|FK → `info_type.id`|
|info|The info itself|—|

### `info_type`
|Column|Description|References|
|---|---|---|
|id|Info type ID|PK|
|description|Description of info type (e.g. genre, runtime, etc.)|—|

### `role_type`
| Column | Description             | References |
| ------ | ----------------------- | ---------- |
| id     | Role type ID            | PK         |
| role   | Description of the role | —          |

