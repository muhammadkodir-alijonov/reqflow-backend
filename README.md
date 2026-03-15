# reqflow

## CD deploy notes

The production deploy workflow (`.github/workflows/deploy.yml`) requires these repository secrets:

- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`
- `SERVER_HOST`
- `SERVER_USER`
- `SERVER_PORT` (optional, defaults to `22`)
- `SERVER_SSH_KEY`
- `SERVER_SSH_PASSPHRASE` (optional, only if key is encrypted)

### `SERVER_SSH_KEY` format

Use the full private key content, including header and footer lines.

Valid examples:

```text
-----BEGIN OPENSSH PRIVATE KEY-----
...
-----END OPENSSH PRIVATE KEY-----
```

or

```text
-----BEGIN RSA PRIVATE KEY-----
...
-----END RSA PRIVATE KEY-----
```

Common failure causes:

- Public key was saved instead of private key.
- Key was saved as a single line with literal `\\n` characters.
- Key content is incomplete (missing BEGIN/END lines).

If deploy fails with `ssh.ParsePrivateKey: ssh: no key found`, re-save `SERVER_SSH_KEY` as a multiline secret and rerun the workflow.