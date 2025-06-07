# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build System & Commands

This is a Scala 3 project using sbt build tool:

- `sbt compile` - Compile the code
- `sbt run` - Run the application (starts HTTP server)
- `sbt test` - Run tests (ZIO Test framework)
- `sbt console` - Start Scala 3 REPL

The project uses Nix flake for development environment with OpenJDK 21 and sbt.

## Architecture

This is a ZIO HTTP-based API server built with Scala 3. Key architectural components:

- **Main Entry Point**: `src/main/scala/morecat/api/Main.scala` - ZIOAppDefault that defines routes and starts the server
- **ZIO Framework**: Uses ZIO 2.1.18 for effect management and ZIO HTTP 3.2.0 for HTTP handling
- **Routes**: Simple HTTP routes defined using ZIO HTTP's Routes DSL
- **Server**: Default ZIO HTTP server configuration

## Code Style

The project has strict compiler settings enabled:
- `-Werror` treats warnings as errors
- `-Wunused:all` warns about unused code
- `-indent` uses significant indentation (Scala 3 style)
- `-explain` provides detailed error explanations

## Dependencies

Managed in `project/Dependencies.scala` with ZIO ecosystem libraries. Test dependencies include ZIO Test, ZIO Test SBT, and ZIO Test Magnolia.
