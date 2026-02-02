# 02 - Load Balancing

## 📚 Theory Notes

### What is Load Balancing?
<!-- Write your notes from the course chapter here -->

### Load Balancing Algorithms
| Algorithm | How it Works | When to Use | when not to use |
|-----------|--------------|-------------|-----------------|

| Round Robin | Requests distributed sequentially | Equal server capacity | Unequal server capacity, long-lived connections, sticky sessions, uneven request distribution |


# Example for long lived connections
 - Websockets (two way street i.e client and server can send messages to each other)
 - Server sent events[SSE] (one way street i.e server to client)
 - gRPC events (this is basically SSE but for backend to backend communication)
 - HTTP keep-alive connections with heavy traffic (this is not a protocol but a feature of HTTP) 
        ~ this is used for reducing latency by reusing the same tcp connection for multiple requests. Keep-alive = reuse the TCP connection
        ~ TCP connect
          GET /api/a
          GET /api/b
          POST /api/c
          (close later)
 - Database connection pools behind a proxy


| Least Connections / Least Active | send to server with fewest active connections | Long-lived connections, sticky sessions, uneven request distribution | Short-lived connections (leading to frequent rebalancing of connections) | 



| weighted round robin [WRR] | assigns weights to each server based on their capacity or performance | Uneven server capacity | Not ideal for high variable loads as WRR does not consider real time server load |  


| weighted least connection [WLC] | takes account both the current load on each server and the relative capacity of each server (weight) | Heterogeneous server env, high traffic web apps, database clusters | Short-lived connections, overhead of tracking active connections |



| IP Hash | Sends requests from the same IP to the same server | Sticky sessions, Stateful applications | Users behind NAT/Proxy (multiple users sharing one IP), dynamic IP addresses, mobile clients switching networks |



| Least Response Time | Sends requests to the server with the lowest response time | Low latency requirements, mixed performance environments | Requires monitoring of response times, not suitable for very short-lived requests |


|Random | Sends requests to a random server | Simple to implement, Homogeneous server env, Stateless Applications | Uneven distribution of requests, not suitable for high variable loads |


|Least Bandwidth | Sends requests to the server with the lowest bandwidth usage (Who is pushing/receiving the fewest bytes right now) | High bandwidth requirements, mixed performance environments | Requires monitoring of bandwidth usage, not suitable for very short-lived requests |



## Need for Loan Balancer -
    - High availability & Fault Tolerance
    - Horizontal Scalability ( As LB acts as Unified Entry point(Virtual IP))
    - Zero downtime deployments (Blue - Green deployments, Canary deployments)
    - Security (SSL Termination, DDoS Protection)
    - Performance Optimization (Caching, Compression)
    

---

### 🔌 HTTP Connection Optimization (Deep Dive on Keep-Alive)

> **Key Insight:** Opening TCP (3-way handshake) + TLS (2-4 round trips) for EVERY request would be terribly wasteful!

#### Connection Lifecycle Evolution

```
HTTP/1.0 (OLD - Wasteful)
─────────────────────────
Request 1: TCP Handshake → TLS → GET /api/a → Close
Request 2: TCP Handshake → TLS → GET /api/b → Close  ← Redundant!
Request 3: TCP Handshake → TLS → POST /api/c → Close ← Redundant!
```

```
HTTP/1.1 with Keep-Alive (DEFAULT since 1999!)
──────────────────────────────────────────────
TCP Handshake → TLS Handshake → 
   GET /api/a (reuse) →
   GET /api/b (reuse) →
   POST /api/c (reuse) →
   ... (idle timeout) → Close
```

#### Key Optimizations in Modern HTTP

| Optimization | What it does | Where |
|--------------|--------------|-------|
| **Keep-Alive** (HTTP/1.1) | Reuses TCP connection for multiple requests | Default ON |
| **Connection Pooling** | Client maintains pool of connections | Browsers, HTTP clients |
| **TLS Session Resumption** | Skip full TLS handshake on reconnect | TLS 1.2/1.3 |
| **HTTP/2 Multiplexing** | Multiple requests on ONE connection simultaneously | Modern browsers |
| **HTTP/3 (QUIC)** | Built on UDP, 0-RTT connection setup | Bleeding edge |

#### Protocol Evolution Timeline

```
┌─────────────────────────────────────────────────────────────┐
│ HTTP/1.0: New TCP + TLS for EVERY request (wasteful)        │
│ HTTP/1.1: Keep-Alive (reuse connection, but sequential)     │
│ HTTP/2:   Multiplexing (parallel requests, single conn)     │
│ HTTP/3:   QUIC/UDP (0-RTT, no head-of-line blocking)        │
└─────────────────────────────────────────────────────────────┘
```

#### Real Example: Loading a Webpage (HTML + CSS + JS)

| Scenario | Time Breakdown | Total |
|----------|----------------|-------|
| **HTTP/1.0** (no keep-alive) | 3x (TCP+TLS: 300ms + GET: 50ms) | ~1050ms 😱 |
| **HTTP/1.1** (keep-alive) | TCP+TLS once: 300ms + 3x GET: 150ms | ~450ms ✅ |
| **HTTP/2** (multiplexing) | TCP+TLS once: 300ms + parallel GET: 50ms | ~350ms 🚀 |

---

### 🔐 TCP + TLS + HTTP/2: The Full Picture

#### Layer Model (Bottom to Top)

```
┌────────────────────────────────────────────────────────────────────┐
│                     APPLICATION LAYER                               │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                     HTTP/2 Protocol                          │  │
│  │  • Multiplexed streams (parallel requests)                   │  │
│  │  • Binary framing (not text like HTTP/1.1)                   │  │
│  │  • Header compression (HPACK)                                │  │
│  │  • Server push                                               │  │
│  └──────────────────────────────────────────────────────────────┘  │
├────────────────────────────────────────────────────────────────────┤
│                     SECURITY LAYER                                  │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                     TLS 1.2/1.3                              │  │
│  │  • Encryption (AES-256-GCM)                                  │  │
│  │  • Authentication (certificates)                             │  │
│  │  • Integrity (HMAC)                                          │  │
│  └──────────────────────────────────────────────────────────────┘  │
├────────────────────────────────────────────────────────────────────┤
│                     TRANSPORT LAYER                                 │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                     TCP                                       │  │
│  │  • Reliable delivery (ACKs, retransmission)                  │  │
│  │  • Ordered delivery (sequence numbers)                        │  │
│  │  • Flow control (window size)                                │  │
│  │  • Congestion control                                        │  │
│  └──────────────────────────────────────────────────────────────┘  │
├────────────────────────────────────────────────────────────────────┤
│                     NETWORK LAYER                                   │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                     IP (IPv4/IPv6)                           │  │
│  │  • Addressing, routing                                       │  │
│  └──────────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────────┘
```

#### Connection Establishment Timeline

```
CLIENT                                                    SERVER
   │                                                         │
   │                   1. TCP HANDSHAKE (3-way)              │
   │  ─────────────── SYN ──────────────────────────────→   │
   │  ←────────────── SYN-ACK ──────────────────────────    │
   │  ─────────────── ACK ──────────────────────────────→   │
   │                                                         │
   │        ~1 RTT (Round Trip Time) = ~50-100ms            │
   │                                                         │
   ├─────────────────────────────────────────────────────────┤
   │                                                         │
   │                   2. TLS HANDSHAKE                      │
   │                                                         │
   │  ─── ClientHello ──────────────────────────────────→   │
   │      (supported ciphers, TLS version, random)          │
   │                                                         │
   │  ←── ServerHello + Certificate + ServerHelloDone ───   │
   │      (chosen cipher, server's public key)              │
   │                                                         │
   │  ─── ClientKeyExchange + ChangeCipherSpec + Finished → │
   │      (encrypted with server's public key)              │
   │                                                         │
   │  ←── ChangeCipherSpec + Finished ──────────────────    │
   │                                                         │
   │        ~2 RTT for TLS 1.2 | ~1 RTT for TLS 1.3         │
   │                                                         │
   ├─────────────────────────────────────────────────────────┤
   │                                                         │
   │              3. HTTP/2 CONNECTION PREFACE               │
   │  ─── Magic string + SETTINGS frame ────────────────→   │
   │  ←── SETTINGS frame ───────────────────────────────    │
   │  ─── SETTINGS ACK ─────────────────────────────────→   │
   │                                                         │
   ├─────────────────────────────────────────────────────────┤
   │                                                         │
   │              4. HTTP/2 MULTIPLEXED REQUESTS             │
   │                                                         │
   │  ─── Stream 1: HEADERS (GET /api/users) ───────────→   │
   │  ─── Stream 3: HEADERS (GET /api/orders) ──────────→   │  ← PARALLEL!
   │  ─── Stream 5: HEADERS (POST /api/data) ───────────→   │
   │                                                         │
   │  ←── Stream 1: HEADERS + DATA ─────────────────────    │
   │  ←── Stream 3: HEADERS + DATA ─────────────────────    │  ← ANY ORDER!
   │  ←── Stream 5: HEADERS + DATA ─────────────────────    │
   │                                                         │
   └─────────────────────────────────────────────────────────┘
```

#### HTTP/1.1 vs HTTP/2 Comparison

```
HTTP/1.1 (Head-of-Line Blocking)
────────────────────────────────
Single TCP Connection:
  Request 1 ───────→ [wait] [wait] [wait] ←─── Response 1
                     Request 2 ──→ [wait] ←─── Response 2
                                   Request 3 → ← Response 3

Time: ████████████████████████████████████ (sequential)


HTTP/2 (Multiplexing on SINGLE Connection)
──────────────────────────────────────────
Single TCP Connection:
  Stream 1: Request ──────────────→ ←── Response
  Stream 3: Request ──────────────→ ←── Response  (parallel!)
  Stream 5: Request ──────────────→ ←── Response

Time: █████████████ (parallel = faster!)
```

#### What Each Layer Does

| Layer | Role | What Gets Added |
|-------|------|-----------------|
| **HTTP/2** | Application logic | Headers, body, method (GET/POST) |
| **TLS** | Encrypt everything above | Encrypted payload, MAC |
| **TCP** | Reliable transport | Sequence #, ACK #, ports |
| **IP** | Routing | Source IP, Dest IP |

#### Packet Encapsulation

```
Original HTTP/2 Data: "GET /api/users"
                            │
                            ▼
┌───────────────────────────────────────────────────────────────────┐
│ Ethernet │   IP    │   TCP   │   TLS   │   HTTP/2 DATA           │
│  Header  │ Header  │ Header  │ Record  │   (encrypted)           │
│  14 B    │  20 B   │  20 B   │  5+ B   │   Variable              │
└───────────────────────────────────────────────────────────────────┘
          │                     │         │
          │                     │         └── Only this is encrypted
          │                     └── Ports (e.g., 443)
          └── IP addresses (e.g., 192.168.1.1 → 93.184.216.34)
```

#### TLS 1.2 vs TLS 1.3

```
TLS 1.2 (2 Round Trips)
───────────────────────
Client ────→ ClientHello
       ←──── ServerHello, Certificate, ServerHelloDone
       ────→ ClientKeyExchange, ChangeCipherSpec, Finished
       ←──── ChangeCipherSpec, Finished
       ────→ [Application Data]                    Total: 2 RTT


TLS 1.3 (1 Round Trip!)
───────────────────────
Client ────→ ClientHello + KeyShare
       ←──── ServerHello + KeyShare + EncryptedExtensions + Finished
       ────→ Finished + [Application Data]         Total: 1 RTT

0-RTT (Resumed Connection):
Client ────→ ClientHello + EarlyData (encrypted!)
       ←──── Response                              Total: 0 RTT!
```

---

### 🧪 EXPERIMENT: Disable Keep-Alive to See Performance Drop

Then use Apache Bench to compare:
```bash
# Test with keep-alive (default)
ab -n 100 -c 10 http://localhost:8080/test

# Test forcing new connections  
ab -n 100 -c 10 -H "Connection: close" http://localhost:8080/test
```

Compare the **Requests per second** and **Time per request** metrics!

---

### 🔗 Connection-Heavy vs Bandwidth-Heavy Servers

Understanding when to use WLC (Weighted Least Connections) vs Least Bandwidth:

| Aspect | Connection-Heavy | Bandwidth-Heavy |
|--------|------------------|-----------------|
| **Bottleneck** | CPU, Memory, File Descriptors | Network I/O, NIC capacity |
| **Typical Apps** | Chat apps, WebSockets, API gateways | Video streaming, file downloads, CDN |
| **Algorithm** | WLC (Weighted Least Connections) | Least Bandwidth |

---

### 🧠 Deep Dive: What Happens Per TCP Connection

#### 1. TCP Socket Buffers (RX and TX)

Every TCP connection has **two kernel buffers**:

```
┌─────────────────────────────────────────────────────────────────────┐
│                        TCP CONNECTION                                │
│  ┌─────────────────┐                    ┌─────────────────┐         │
│  │   RX Buffer     │   ← Network ←      │   TX Buffer     │         │
│  │  (Receive)      │                    │  (Transmit)     │         │
│  │                 │                    │                 │         │
│  │ Incoming data   │                    │ Outgoing data   │         │
│  │ waits here for  │                    │ waits here for  │         │
│  │ app to read     │                    │ kernel to send  │         │
│  └─────────────────┘                    └─────────────────┘         │
│         ↓                                       ↑                    │
│    app.read()                              app.write()               │
└─────────────────────────────────────────────────────────────────────┘
```

| Buffer | Direction | Purpose | Default Size |
|--------|-----------|---------|--------------|
| **RX (Receive)** | Network → App | Holds incoming data until `read()` | 87KB - 6MB |
| **TX (Transmit)** | App → Network | Holds outgoing data until ACK received | 16KB - 4MB |

**Why buffers matter:**
- If RX buffer fills up → **TCP flow control kicks in** (sender slows down)
- If TX buffer fills up → **`write()` blocks** (app waits)
- 10,000 connections × 128KB = **1.28 GB just for buffers!**

```bash
# Check buffer sizes on Linux
cat /proc/sys/net/ipv4/tcp_rmem  # RX buffer: min, default, max
cat /proc/sys/net/ipv4/tcp_wmem  # TX buffer: min, default, max
```

#### 2. File Descriptors (FD) - Yes, 1 FD per TCP Connection!

**What is a File Descriptor?**
In Unix/Linux, **everything is a file** - including network sockets. Each open file/socket gets an integer ID called a File Descriptor.

```
┌─────────────────────────────────────────────────────────────────────┐
│ Process FD Table                                                     │
│ ┌────────┬────────────────────────────────────────────────────────┐ │
│ │ FD 0   │ stdin (keyboard input)                                 │ │
│ │ FD 1   │ stdout (terminal output)                               │ │
│ │ FD 2   │ stderr (error output)                                  │ │
│ │ FD 3   │ open("config.txt")                                     │ │
│ │ FD 4   │ socket() → TCP connection to client 1                  │ │
│ │ FD 5   │ socket() → TCP connection to client 2                  │ │
│ │ FD 6   │ socket() → TCP connection to client 3                  │ │
│ │ ...    │ ...                                                    │ │
│ └────────┴────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
```

**Why FDs matter:**
```
Default limit: 1,024 FDs per process
Error when exceeded: "Too many open files"

Each TCP connection = 1 socket = 1 FD
10,000 connections = 10,000 FDs needed!
```

```bash
# Check FD limits
ulimit -n                     # Current soft limit (usually 1024)
ulimit -Hn                    # Hard limit (max you can set)
cat /proc/sys/fs/file-max     # System-wide max

# Increase for a process
ulimit -n 65536

# Check FDs used by a process
ls /proc/<PID>/fd | wc -l
lsof -p <PID> | wc -l
```

#### 3. Kernel TCP State Machine (SYN, ACK, FIN)

The kernel tracks **state per connection**. This state machine is why connections are expensive:

```
TCP Connection States:
┌──────────────────────────────────────────────────────────────────────┐
│                                                                      │
│  CLIENT                          SERVER                              │
│    │                               │                                 │
│    │ ──── SYN ──────────────────→ │  (LISTEN → SYN_RECEIVED)        │
│    │ ←─── SYN-ACK ─────────────── │                                 │
│    │ ──── ACK ──────────────────→ │  (SYN_RECEIVED → ESTABLISHED)   │
│    │                               │                                 │
│    │ ←───── DATA ────────────────→│  ESTABLISHED (data transfer)    │
│    │                               │                                 │
│    │ ──── FIN ──────────────────→ │  (ESTABLISHED → FIN_WAIT_1)     │
│    │ ←─── ACK ─────────────────── │  (CLOSE_WAIT)                   │
│    │ ←─── FIN ─────────────────── │  (LAST_ACK)                     │
│    │ ──── ACK ──────────────────→ │  (TIME_WAIT → CLOSED)           │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘

Key States:
• LISTEN       - Server waiting for connections
• SYN_RECEIVED - Server got SYN, sent SYN-ACK, waiting for ACK
• ESTABLISHED  - Connection active, data can flow
• TIME_WAIT    - Connection closed, waiting 2×MSL (60-120s) before reuse
• CLOSE_WAIT   - Received FIN, waiting for app to close
```

**Why kernel state matters:**
| State | Memory Cost | Problem |
|-------|-------------|---------|
| `SYN_RECEIVED` | ~300 bytes | SYN flood attacks fill this queue |
| `ESTABLISHED` | ~1KB + buffers | Each active connection costs memory |
| `TIME_WAIT` | ~300 bytes | Lingers 60-120s, can exhaust ports! |

```bash
# View connection states
netstat -ant | awk '{print $6}' | sort | uniq -c | sort -rn
ss -s  # Summary of socket states

# Common output:
# 5000 TIME_WAIT    ← Closed connections waiting to expire
# 1000 ESTABLISHED  ← Active connections
# 50   CLOSE_WAIT   ← App hasn't closed socket yet (potential bug!)
```

---

### 🔄 TCP Connection vs Request vs Thread (The Big Picture)

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              OPERATING SYSTEM                                    │
│  ┌──────────────────────────────────────────────────────────────────────────┐   │
│  │                           PROCESS (JVM)                                   │   │
│  │   PID: 1234                                                               │   │
│  │   Memory: Heap + Stack                                                    │   │
│  │                                                                           │   │
│  │  ┌─────────────────────────────────────────────────────────────────────┐ │   │
│  │  │                      THREAD POOL (200 threads)                      │ │   │
│  │  │                                                                     │ │   │
│  │  │  ┌─────────┐  ┌─────────┐  ┌─────────┐       ┌─────────┐          │ │   │
│  │  │  │Thread-1 │  │Thread-2 │  │Thread-3 │  ...  │Thread-N │          │ │   │
│  │  │  │         │  │         │  │         │       │         │          │ │   │
│  │  │  │ Handles │  │ Handles │  │ Handles │       │ Waiting │          │ │   │
│  │  │  │ Req A   │  │ Req B   │  │ Req C   │       │ (idle)  │          │ │   │
│  │  │  └────┬────┘  └────┬────┘  └────┬────┘       └─────────┘          │ │   │
│  │  │       │            │            │                                  │ │   │
│  │  └───────┼────────────┼────────────┼──────────────────────────────────┘ │   │
│  │          │            │            │                                     │   │
│  │  ┌───────┴────────────┴────────────┴──────────────────────────────────┐ │   │
│  │  │                    TCP CONNECTIONS (FDs)                            │ │   │
│  │  │  FD:4          FD:5          FD:6          FD:7                     │ │   │
│  │  │  Client A      Client B      Client A      Client C                 │ │   │
│  │  │  Request 1     Request 1     Request 2     Request 1                │ │   │
│  │  └─────────────────────────────────────────────────────────────────────┘ │   │
│  └──────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
```

#### Key Relationships:

| Concept | What It Is | 1:1 or 1:Many? |
|---------|------------|----------------|
| **Process** | JVM instance running your Spring Boot app | 1 process = many threads |
| **Thread** | Execution unit that handles a request | 1 thread = 1 request at a time |
| **Thread Pool** | Fixed set of reusable threads | Spring default: 200 threads |
| **TCP Connection** | Pipe between client and server (FD) | 1 connection = 1 FD |
| **Request** | HTTP request (GET/POST/etc) | 1 connection = many requests (keep-alive) |

#### Three Different Models:

**Model 1: Thread-per-Request (Traditional Spring MVC)**
```java
// Each HTTP request = 1 thread blocked until response sent
@GetMapping("/api/data")
public Data getData() {
    return service.fetchData();  // Thread blocked during DB call
}
```
```
Connection 1 ──→ [Request A] ──→ Thread-1 (blocked 100ms)
             ──→ [Request B] ──→ Thread-2 (blocked 100ms)  
             ──→ [Request C] ──→ Thread-3 (blocked 100ms)
             
200 concurrent requests = 200 threads needed
201st request = QUEUED (waiting for free thread)
```

**Model 2: Thread-per-Connection (Old blocking I/O)**
```
Connection 1 ──→ Thread-1 (dedicated for life of connection)
Connection 2 ──→ Thread-2 
Connection 3 ──→ Thread-3

10,000 connections = 10,000 threads = ❌ RAM explosion
```

**Model 3: Event Loop (Netty, WebFlux, Node.js)**
```
               ┌──────────────────────────────────────┐
               │        EVENT LOOP (1 thread)         │
               │                                       │
Connection 1 ──┤  Handles ALL connections with        │
Connection 2 ──┤  non-blocking I/O                    │
Connection 3 ──┤                                       │
    ...      ──┤  Callbacks when data ready           │
Connection N ──┤                                       │
               └──────────────────────────────────────┘

10,000 connections = 1-4 threads = ✅ Super efficient!
```

**Spring Boot Defaults (Tomcat):**
```properties
# application.properties
server.tomcat.threads.max=200       # Max worker threads
server.tomcat.threads.min-spare=10  # Always keep 10 ready
server.tomcat.max-connections=8192  # Max TCP connections
server.tomcat.accept-count=100      # Queue when all connections busy
```

---

### 📊 Resource Limits Cheat Sheet

```
Per Connection Cost:
┌──────────────────────────────────────────────────────┐
│ • Socket buffers (RX+TX): 128KB - 512KB             │
│ • File Descriptor: 1 FD                              │
│ • Kernel TCP state: ~1KB                             │
│ • Thread (if thread-per-connection): 1MB stack       │
└──────────────────────────────────────────────────────┘

Typical Limits:
┌──────────────────────────────────────────────────────┐
│ • FDs per process: 1,024 (default) → 65,536 (tuned) │
│ • Threads per process: ~1,000-10,000                 │
│ • Ephemeral ports: 28,232 (32768-60999)             │
│ • TIME_WAIT sockets: Can exhaust ports!             │
└──────────────────────────────────────────────────────┘
```

---

### Stateless vs Stateful
<!-- Your notes -->

### High Availability & Fault Tolerance
<!-- Your notes -->

---

## Types of Load Balancers 

 - Hardware Load Balancing (Aplication specific integrated circuits[ASICs], Field Programmable Gate Arrays[FPGAs])
 - Software Load Balancing (NGINX, HAProxy, Apache HTTP Server)
 - Cloud Load Balancing (AWS ELB, Google Cloud Load Balancing, Azure Load Balancer)
 - DNS Load Balancing (Route 53, Cloudflare DNS)
 - Global Server Load Balancing (GSLB) - DNS based load balancing across multiple data centers. It combines DNS load balancing with health checks and routing policies to direct traffic to the most appropriate data center.
 - Layer 4 Load Balancing (TCP/UDP)
 - Layer 7 Load Balancing (HTTP/HTTPS)

---

## � Layer 4 vs Layer 7 Load Balancing (Deep Dive)

### The OSI Model Context

```
┌─────────────────────────────────────────────────────────────────────┐
│ Layer 7 - Application   │ HTTP, HTTPS, WebSocket, gRPC             │ ← L7 LB
├─────────────────────────────────────────────────────────────────────┤
│ Layer 4 - Transport     │ TCP, UDP (ports, connections)             │ ← L4 LB
├─────────────────────────────────────────────────────────────────────┤
│ Layer 3 - Network       │ IP (routing, addressing)                  │
└─────────────────────────────────────────────────────────────────────┘
```

### Quick Comparison

| Feature | L4 Load Balancer | L7 Load Balancer |
|---------|------------------|------------------|
| **Operates at** | TCP/UDP | HTTP/HTTPS |
| **Can see** | IP, Port, Protocol | Headers, URL, body |
| **TLS Termination** | No (passes through) | Yes (decrypts) |
| **Speed** | ⚡ Very fast | Slower (parsing) |
| **TCP Connections** | Same (passthrough) | New (proxy) |
| **Example** | AWS NLB | NGINX, AWS ALB |

---

### 🔌 Connection Handling: The Critical Difference

#### L4 LB: SAME TCP Connection (Passthrough)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  Client                    L4 LB                         Backend            │
│       │                        │                              │             │
│       │ ══════ TCP SYN ═══════>│ ════ TCP SYN ═══════════════>│             │
│       │<══════ TCP SYN-ACK ════│<════ TCP SYN-ACK ════════════│             │
│       │ ══════ DATA ══════════>│ Just rewrites IP/Port        │             │
│       │                        │ ════ DATA ══════════════════>│             │
│       │<══════ RESPONSE ═══════│<════ RESPONSE ═══════════════│             │
│                                                                             │
│   ONE CONTINUOUS TCP CONNECTION                                             │
│   LB modifies packet headers only - doesn't read content                    │
└─────────────────────────────────────────────────────────────────────────────┘
```

#### L7 LB: TWO Separate TCP Connections (Proxy)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  Client                         L7 LB                        Backend        │
│       │                            │                              │         │
│       │ ════ TCP HANDSHAKE 1 ═════>│                              │         │
│       │<════ TCP HANDSHAKE 1 ══════│                              │         │
│       │ ════ TLS HANDSHAKE ═══════>│                              │         │
│       │<════ TLS HANDSHAKE ════════│                              │         │
│       │ ══ ENCRYPTED REQUEST ═════>│                              │         │
│       │                            │                              │         │
│       │                   ┌────────┴────────┐                     │         │
│       │                   │ DECRYPTS & READS│                     │         │
│       │                   │ GET /api/users  │                     │         │
│       │                   │ Host: api.com   │                     │         │
│       │                   └────────┬────────┘                     │         │
│       │                            │                              │         │
│       │                            │ ════ TCP HANDSHAKE 2 ═══════>│         │
│       │                            │ ════ HTTP REQUEST ══════════>│         │
│       │                            │<════ HTTP RESPONSE ══════════│         │
│       │<══ ENCRYPTED RESPONSE ═════│                              │         │
│                                                                             │
│   CONNECTION 1                                    CONNECTION 2              │
│   Client ←──────────────→ LB ←─────────────────────────→ Backend            │
│                                                                             │
│   TWO COMPLETELY SEPARATE TCP CONNECTIONS!                                  │
└─────────────────────────────────────────────────────────────────────────────┘
```

#### Why L7 MUST Create New Connections (Analogy)

```
L4 LB (Forwarding Service):
┌─────────────────┐     ┌──────────────┐     ┌─────────────────┐
│   Sealed        │ ──→ │  Just looks  │ ──→ │   Same Sealed   │
│   Envelope      │     │  at address  │     │   Envelope      │
└─────────────────┘     └──────────────┘     └─────────────────┘
                        Doesn't open it!


L7 LB (Translation Service):
┌─────────────────┐     ┌──────────────────────┐     ┌─────────────────┐
│   Sealed        │ ──→ │   Opens envelope     │ ──→ │   NEW Envelope  │
│   Envelope      │     │   Reads content      │     │   with content  │
└─────────────────┘     │   Decides recipient  │     └─────────────────┘
                        └──────────────────────┘
                        MUST open to read!
```

---

### 🤔 Is NGINX L4 or L7?

**NGINX can be BOTH!**

```nginx
# L7 Mode (http block) - Most common
http {
    upstream backend {
        server backend1:8080;
        server backend2:8080;
    }
    
    server {
        listen 80;
        location /api/ {
            proxy_pass http://backend;  # Routes based on URL
        }
    }
}

# L4 Mode (stream block) - Raw TCP
stream {
    upstream mysql {
        server db1:3306;
        server db2:3306;
    }
    
    server {
        listen 3306;
        proxy_pass mysql;  # Just forwards TCP packets
    }
}
```

| NGINX Mode | Layer | Use Case |
|------------|-------|----------|
| `http {}` | L7 | Web apps, APIs |
| `stream {}` | L4 | Databases, Redis |

---

### 📊 Use Cases

#### L4 Use Cases
| Use Case | Why L4? |
|----------|---------|
| Database (MySQL) | Can't parse SQL protocol |
| Redis/Memcached | Binary protocol |
| Gaming servers | UDP, low latency |
| TLS passthrough | Backend terminates TLS |

#### L7 Use Cases
| Use Case | Why L7? |
|----------|---------|
| Path routing | `/api/*` → API, `/web/*` → Web |
| Host routing | `api.com` → API service |
| A/B testing | Route % based on header/cookie |
| Rate limiting | Per-user based on API key |
| SSL termination | Offload TLS from backends |

---

### Real-World: Using Both!

```
Internet ──→ L4 LB (NLB) ──→ L7 LB (NGINX) ─┬→ API Service
             │                  │            ├→ Auth Service
             │                  │            └→ Web App
             │                  │
             │                  └── SSL termination, routing
             └── Handles millions of connections efficiently

AWS: NLB (L4) + ALB (L7)
```

---


## statefull vs stateless Load Balancing
    -- Stateless: stateless is when the load balancer does not store any information about the client. It treats each request as a new request. 
    -- Stateful: stateful is when the load balancer stores information about the client. It uses this information to route the client to the same server for all requests.


## sticky sessions
    -- Sticky sessions, also known as session affinity, are a way to ensure that all requests from a particular client are sent to the same server. This is useful for applications that maintain session state, such as shopping carts or user sessions.
    -- Sticky sessions can be implemented in a number of ways, such as using cookies, IP addresses, or other identifiers.


## Redundancy and Failover strategies for Load Balancers

 -- to ensure high availability and fault tolerance.
    -- Redundancy can be achieved through several failover strategies.
        -- Active-Passive: active load balancer handles all the traffic, while the passive load balancer is in standby mode. If the active load balancer fails, the passive load balancer takes over.
        -- Active-Active: both load balancers are active and handle traffic. If one fails, the other takes over. This configuration provided better resource utilization and increased fault tolerance compared to the active-passive configuration.
 -- health checks and monitoring are effective components of high availability and fault tolerance for LB.


## Synchronization and State Sharing

In stateful load balancing, multiple LB instances must stay in sync to ensure consistent session routing.

### What Needs to Be Shared?
- **Session IDs** - Which client belongs to which session
- **Session Data** - User preferences, cart contents, auth tokens
- **Session State** - Active/expired, last accessed time

### State Sharing Mechanisms

| Mechanism | Pros | Cons |
|-----------|------|------|
| **Shared Database** (Redis, MySQL) | Simple, reliable | Network latency, DB becomes bottleneck |
| **Distributed Cache** (Redis Cluster) | Fast, scalable | Complexity in setup |
| **Shared File System** | Simple | Slow, not scalable |
| **Gossip Protocol** | No single point of failure | Eventually consistent |

### Centralized State Management Pattern

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   LB 1      │     │   LB 2      │     │   LB 3      │
└──────┬──────┘     └──────┬──────┘     └──────┬──────┘
       │                   │                   │
       └───────────────────┼───────────────────┘
                           │
                           ▼
              ┌────────────────────────┐
              │   Centralized State    │
              │   Store (Redis/etcd)   │
              └────────────────────────┘
```

### Popular Tools for State Sharing
- **Redis** - Fast in-memory store, great for session data
- **etcd** - Distributed key-value store (used by Kubernetes)
- **Consul** - Service discovery + KV store
- **ZooKeeper** - Coordination service for distributed systems

> ⚠️ **Single Point of Failure**: Centralized stores can become SPOFs. Use clustering/replication (Redis Sentinel, etcd cluster) for high availability.


## 🛠️ Hands-On Project

**Goal:** Set up NGINX as a load balancer for multiple Spring Boot instances

### Project Structure
```
02-load-balancing/
├── README.md              # This file (notes + project info)
├── service/               # Spring Boot app
├── nginx/
│   └── nginx.conf         # Load balancer config
├── docker-compose.yml     # Run 3 instances + NGINX
└── LEARNINGS.md           # Post-project reflections
```

### Tasks
- [ ] Create simple Spring Boot service returning instance ID
- [ ] Configure NGINX with Round Robin
- [ ] Test failover by killing instances
- [ ] Try different algorithms

---

## 💡 Key Takeaways
<!-- Fill after completing the module -->
1. 
2. 
3. 

## ❓ Interview Questions
- What happens if a load balancer fails?
- How do you handle sticky sessions?
- Difference between L4 and L7 load balancing?
