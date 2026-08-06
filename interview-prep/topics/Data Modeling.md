## Data types and shapes
**Money as signed integer cents**: avoids floating point rounding error
- good for handling thousands of transactions that drift into real accounting discrepancies
- signed cents allow you to represent refunds/debits without a separate sign-flag column
- Why not `NUMERIC`?
	- `NUMERIC(19, 4)` is defensible, especially if Foreign Exchange (FX) or sub-cent precision ever applies (fractional cents)
- integer cents also keeps arithmetic exact and avoids decimal-library overhead for the common case

## Idempotency
Use **unique external IDs** for guaranteeing non-overlapping atomic write locks. This guarantees a single DB transaction can succeed for a given ID.

The **App-Layer Gap**: when two identical webhook requests arrive concurrently across different web servers, they first read the DB at `t1`, find nothing there, then both proceed to write at `t2`.

**Solution**: with a `UNIQUE(provider, external_id` constraint, the DB engine enforces *serialization* using row/index locks.
- this prevents duplicate writes at the storage layer, forcing the DB as the *ground truth* for every write
- the unique constraint forces Service B to wait for Service A's transaction to commit before throwing the constraint violation

## Handling discrepancies
**Append-only over destructive edits**: don't erase mistakes -> post a correcting entry instead

**Nullable dual-FK for discrepancies**: instead of 3 separate tables for `unmatched bank transaction`, `unmatched journal line`, and `matched pair`, use *one* table with two *nullable* FKs.
- this captures all 3 states in the *nullness* pattern itself.
- tradeoff: you lose `NOT NULL` FK constraints on either side

**Effective/posted date vs `created_at`**: Transactions can be recorded today with an effective action date in the past, but for production models, you *need* both -- the audit timestamp (record date) and the business timestamp (action date)
- people default to one timestamp, but that's a red flag

**Soft delete, never hard delete for financial records**: add a `voided_at`, `voided_by`, `void_reason` when dealing with removable records. `active` becomes `voided_at IS NULL`.
- consistent with append-only design
- when deleting a transaction or record, default to `POST /invoices/{id}/void` rather than `DELETE /invoices/{id}` since `DELETE` doesn't have a body, and you need metadata (`voided_by`, `void_reason`, timestamp) to store with the soft deletion logic.
	- in this way, you have the option to append these field values to the table
	- contract stays the same, but the implementation is a bit different on the backend
