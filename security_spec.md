# Security Specification & Threat Model — AIS Capital Terminal

## 1. Data Invariants
- Invariant 1: Authenticated Identity Integrity: Every document written (`leads`, `audits`, `invoices`, `deposits`) must have `userId == request.auth.uid`. No user may forge another user's `userId`.
- Invariant 2: Immortality of Ownership: On updates, `incoming().userId == existing().userId`. Re-assigning resource ownership is strictly rejected.
- Invariant 3: PII & Financial Record Isolation: Only the document creator or an authorized administrator (`admins/{uid}`) can read and write pipeline leads, invoices, and bank transaction slips.
- Invariant 4: Bounded Payload Sizes: Strings must not exceed maximum length constraints (e.g. `companyName <= 128`, `notes <= 1000`) preventing denial-of-wallet resource inflation attacks.
- Invariant 5: Status Enum Enforcement: Lead and invoice statuses can only transition across defined valid states.
- Invariant 6: Default Deny: All undefined paths and wildcard documents are explicitly denied (`allow read, write: if false;`).

## 2. The "Dirty Dozen" Threat Payloads
1. Spoofed Author Write: Attacker attempts to create a lead with `userId: "victim_uid"`. Expected: `PERMISSION_DENIED`.
2. Ghost Field Injection: Attacker attempts to inject `isAdmin: true` into a lead or user document. Expected: `PERMISSION_DENIED`.
3. Unauthenticated Read: Unauthenticated client attempts to query `/invoices` or `/deposits`. Expected: `PERMISSION_DENIED`.
4. Over-sized String Attack: Attacker sends a 1.2MB payload in `customHook` or `companyName`. Expected: `PERMISSION_DENIED`.
5. ID Poisoning: Attacker attempts to write to a path with malicious characters like `/leads/../../../etc/passwd`. Expected: `PERMISSION_DENIED`.
6. Self-Promoted Admin: Non-admin user tries to write directly to `/admins/{uid}`. Expected: `PERMISSION_DENIED`.
7. Cross-Tenant Lead Read: User A queries individual lead owned by User B. Expected: `PERMISSION_DENIED`.
8. Invoice Amount Mutation by Non-Owner: User B attempts an update on User A's invoice. Expected: `PERMISSION_DENIED`.
9. Ownership Theft: User updates an existing lead attempting to mutate `userId` to their own. Expected: `PERMISSION_DENIED`.
10. Malformed Enum Status: Client tries writing `status: "HACKED"` on a Mushak invoice. Expected: `PERMISSION_DENIED`.
11. Blank Query Scraping: Client issues an unconstrained `list` request across all users' leads. Expected: `PERMISSION_DENIED`.
12. Negative Financial Values: Client tries writing negative total invoice amounts. Expected: `PERMISSION_DENIED`.
