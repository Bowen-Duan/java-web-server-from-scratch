# Java Web Server From Scratch

A working HTTP web server written in pure Java — no frameworks, no external libraries, just `java.net.ServerSocket` and the JDK. Built step by step through eight progressive levels during enterprise training coursework, it grows from accepting a single socket connection into a small servlet-style server with a thread pool, HTTP request parsing, static file serving with MIME types, and dynamic servlet dispatch through reflection.

## The eight levels

| Level | What it adds | Code |
|-------|--------------|------|
| 01–03 | Raw `ServerSocket` server: accept connections, read/write bytes, first responses | 1 file each |
| 04 | A per-connection `Task` so request handling is separated from the accept loop | 2 files |
| 05 | Externalised configuration (`.properties`) and shared constants | 4 files |
| 06 | A `Request` abstraction: parsing the request line and headers | 4 files |
| 07 | A `Response` abstraction and MIME-type parsing for static files | 8 files |
| 08 | Full structure: thread pool, config-driven port/threads, static + dynamic (servlet) handling, a `Servlet` interface and a `LoginServlet` | 10 files, 820 lines |

Total: 31 Java files, ~2,500 lines. Each level is kept as its own package (`com.briup.level01` … `level08`) so the progression stays readable — diffing two neighbouring levels shows exactly what one new idea changed.

## How level08 works

```
Browser ──► ServerSocket (port from config)
              │  accept()
              ▼
        Fixed thread pool ──► Task (one per connection)
              │
              ├─ RequestImpl   parses request line, headers, query parameters
              └─ ResponseImpl  static path → file under webroot/ + MIME type + status line
                               dynamic path → look up servlet class in servlet.properties,
                                              instantiate reflectively, call servletHandler()
```

- **Configuration first** — port, thread count, web root, index/error pages, MIME mappings, status phrases and servlet mappings all live in `config/*.properties` and are loaded by `LoadArgsUtils`. (In the original coursework these were hard-coded `D:\` paths; they are relative paths now, so the server runs anywhere.)
- **Static serving** — the request path is mapped onto `webroot/`; the response status line and `Content-Type` are assembled from the status-code and MIME property files; a missing file falls back to the configured 404 page.
- **Dynamic servlets** — paths registered in `config/servlet.properties` are dispatched to a class implementing the `Servlet` interface. The bundled `LoginServlet` reads the `username` query parameter and writes a raw HTTP response by hand — status line, headers, blank line, body — which is the whole point of the exercise: seeing exactly what a servlet container normally hides.

## Run it (level08)

Requires only a JDK (no build tool, no dependencies):

```bash
# from the project root
javac -d out $(find src/com/briup/level08 -name '*.java')
java -cp out com.briup.level08.Server

# then open http://localhost:9999/            (static index page)
# or         http://localhost:9999/login?username=Bowen   (dynamic servlet)
```

Port and thread-pool size are set in `config/config.properties`.

## What this demonstrates

- Socket-level network programming and the HTTP/1.1 request/response format, byte for byte
- Parsing request lines, headers and query strings by hand
- Concurrent connection handling with a bounded thread pool
- The core idea behind a servlet container: a `Servlet` interface, a request/response pair, and reflection-based dispatch driven by configuration

## Author

Bowen Duan — B.Eng. Software Engineering, Shanxi Agricultural University. Written during Briup enterprise training coursework as a from-first-principles study of how web servers and servlet containers work.

## License

MIT
