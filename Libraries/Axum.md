---
tags:
  - rust
  - backend
  - web
  - async
created: 2025-06-27 00:47
---
# Axum
Axum is an ergonomic async-first web framework built on Tokio and Tower. It strikes a balance between type-safe routing and ease of use, great for both beginners and advanced Rust devs :contentReference[oaicite:5]{index=5}.

### What I Tried
- Defined typed routes and dependency injection using extractors
- Handled async database calls with SQLx
- Composed middleware for auth and logging

### Strengths
- Clear, modular routing and handler composition
- Built on solid foundations (Tokio, Tower)
- Lower friction for small to medium API services

### Weaknesses
- Async patterns can be unfamiliar (lifetimes, ownership)
- Still growing ecosystem compared to Actix

### Status
- Stability: Emerging as Rust’s go-to web framework
- Utility: Perfect for scalable, async services
- Community: Fast-growing and well-supported
