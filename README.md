# AMTop-Ecommerce

AMTop-Ecommerce is a full-stack e-commerce application with a React + TypeScript frontend, a Spring Boot backend, and a MySQL database. The project is now containerized and should be run with Docker Compose instead of the old manual frontend/backend startup flow.

## Table of Contents

- [Overview](#overview)
- [Project Stack](#project-stack)
- [Prerequisites](#prerequisites)
- [Environment Variables](#environment-variables)
- [Run With Docker Compose](#run-with-docker-compose)
- [Access the App](#access-the-app)
- [Database Access](#database-access)
- [Useful Commands](#useful-commands)
- [Contributors](#contributors)
- [Video Demo](#video-demo)

## Overview

The application provides separate customer and vendor/admin experiences, including product browsing, cart and wishlist flows, order management, reviews, and administration features. The current setup is designed to be reproducible with a single Docker Compose command.

## Project Stack

- Frontend: React, TypeScript, Vite, Bootstrap, React Router
- Backend: Spring Boot, Spring Security, Spring Data JPA, JWT, Maven
- Database: MySQL 8
- Deployment: Docker, Docker Compose, Nginx

## Prerequisites

Install the following before running the project:

- Docker Engine
- Docker Compose plugin

You do not need to install Node.js, Java, or MySQL locally to run the application containers.

## Environment Variables

Create a root `.env` file if you do not already have one. The project uses it for backend and database configuration. The important values are:

- `MYSQL_ROOT_PASSWORD`
- `MYSQL_DATABASE`
- `MYSQL_USER`
- `MYSQL_PASSWORD`
- `SPRING_MAIL_USERNAME`
- `SPRING_MAIL_PASSWORD`
- `JWT_SECRET`

If you already have a `.env`, keep it in the repository root next to `docker-compose.yml`.

## Run With Docker Compose

From the repository root, build and start the full stack with:

```bash
docker compose up --build -d
```

This starts:

- `db` on MySQL 8
- `backend` on port `9080`
- `frontend` on port `3000`

To view logs:

```bash
docker compose logs -f
```

To rebuild only one service after a code change:

```bash
docker compose build frontend
docker compose up -d frontend
```

or:

```bash
docker compose build backend
docker compose up -d backend
```

## Access the App

After the stack starts successfully, open:

- Frontend: http://localhost:3000
- Backend: http://localhost:9080

The frontend is served through Nginx and proxies API requests to the backend.

## Database Access

The database container is published on host port `3307` to avoid conflicts with any local MySQL service. Use the following settings from VS Code DB tools or any MySQL client:

- Host: `127.0.0.1`
- Port: `3307`
- Database: `amtopdb`
- User: `amtop`
- Password: `amtop_pass` unless you changed it in `.env`

If you need the root account, use the root password defined in `.env`.

## Useful Commands

Stop the stack:

```bash
docker compose down
```

Stop and remove volumes as well:

```bash
docker compose down -v
```

Check container status:

```bash
docker compose ps
```

Recreate the database container only:

```bash
docker compose up -d --no-deps --force-recreate db
```

## Contributors

- [Adel Mahmoud](https://github.com/Adel-Mahmoud-Mohamed)
- [Mohamed Hassan](https://github.com/mohamedhassan279)
- [Mohamed Hassan](https://github.com/Mohamed-Eltobgy)
- [Mohamed Raffek](https://github.com/mohamedraffeek)
- [Mahmoud Attia](https://github.com/mahmoudattia12)
- [Mahmoud Ghlab](https://github.com/Mahmoudghlab25)

## Video Demo

https://drive.google.com/file/d/14RQTeRMq_MmxQyV1Tj_lC0A-O73ntin8/view?usp=drive_link
