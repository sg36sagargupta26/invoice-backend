# Invoice Backend

A full-stack invoice total calculator with currency conversion. Built with **Quarkus** (Java backend) and **Next.js** (React frontend with MUI).

The backend accepts an invoice with line items in different currencies, converts them to a target currency using [Frankfurter API](https://www.frankfurter.app/) exchange rates, and returns the total.

---

## Architecture

```
┌────────────────────┐      POST /invoice/total      ┌──────────────────────┐
│                    │ ──────────────────────────►   │                      │
│   Next.js UI       │                               │   Quarkus Backend    │
│   (localhost:3000) │ ◄──────────────────────────   │   (localhost:8080)   │
│                    │       JSON response            │                      │
└────────────────────┘                               └───────┬──────────────┘
                                                              │
                                                              │  GET /latest
                                                              ▼
                                                   ┌──────────────────────┐
                                                   │  Frankfurter API     │
                                                   │  (exchange rates)    │
                                                   └──────────────────────┘
```

---

## Prerequisites

- **Java 25+** (the backend uses `maven.compiler.release` set to 25)
- **Maven** (or use the included `./mvnw` wrapper)
- **Node.js 20+** (for the frontend)
- **npm** or **yarn**

---

## Running the Backend (Quarkus)

```shell
# Start in dev mode with live reload
./mvnw quarkus:dev
```

The backend will be available at **http://localhost:8080**.

### Backend API

| Method | Endpoint           | Description                                           |
|--------|--------------------|-------------------------------------------------------|
| POST   | `/invoice/total`   | Calculate invoice total with currency conversion       |

#### Example Request

```json
{
  "invoice": {
    "currency": "EUR",
    "date": "2025-05-23",
    "lines": [
      { "description": "Consulting", "currency": "USD", "amount": 1000 },
      { "description": "Hosting",    "currency": "EUR", "amount": 200  },
      { "description": "Domain",     "currency": "GBP", "amount": 50   }
    ]
  }
}
```

#### Example Response

```json
{
  "total": 1278.45
}
```

### Running Backend Tests

```shell
./mvnw test
```

---

## Running the Frontend (Next.js)

```shell
cd invoice-ui

# Install dependencies (first time only)
npm install

# Start development server
npm run dev
```

The frontend will be available at **http://localhost:3000**.

### Frontend Environment Variables

Create `invoice-ui/.env.local` (already provided):

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

### Building for Production

```shell
cd invoice-ui
npm run build
npm start
```

---

## Running Both Together (Quick Start)

Open **two terminal tabs**:

| Tab 1 — Backend                          | Tab 2 — Frontend                       |
|------------------------------------------|----------------------------------------|
| `./mvnw quarkus:dev`                     | `cd invoice-ui && npm run dev`         |

Then open **http://localhost:3000** in your browser.

---

## CORS Configuration

The backend is pre-configured to accept requests from `http://localhost:3000` (see `src/main/resources/application.properties`).

---

## Tech Stack

| Layer      | Technology                                                |
|------------|-----------------------------------------------------------|
| Backend    | Quarkus 3.35, Java 25, RESTEasy Reactive, Jackson         |
| Frontend   | Next.js 16, React 19, TypeScript, MUI 9 (Material UI)    |
| API Client | Frankfurter API (exchange rates)                          |

---

## Project Structure

```
├── pom.xml                          # Maven build (backend)
├── src/
│   ├── main/java/org/acme/
│   │   ├── InvoiceResource.java     # REST endpoint
│   │   ├── InvoiceService.java      # Business logic
│   │   └── FrankfurterApi.java      # REST client for exchange rates
│   └── test/java/org/acme/          # Backend tests
├── invoice-ui/
│   ├── package.json                 # Node deps (frontend)
│   ├── src/
│   │   ├── app/page.tsx             # Main page (invoice form)
│   │   ├── services/api.ts          # API service layer
│   │   ├── types/invoice.ts         # TypeScript interfaces
│   │   └── theme.ts                 # MUI theme customization
│   └── .env.local                   # API URL config
└── README.md
```

---

## Deployment & Operations

### Frontend Hosting — Vercel

**Vercel** (the company behind Next.js) is the recommended platform for the frontend.

| Reason | Detail |
|--------|--------|
| **First-class Next.js support** | Vercel is built by the creators of Next.js and offers zero-config deployment, automatic server-side rendering, static generation, and ISR (Incremental Static Regeneration). |
| **Edge Network** | Globally distributed CDN ensures low-latency delivery of static assets and server-rendered pages. |
| **Preview Deployments** | Every branch or PR automatically gets a unique preview URL, enabling instant review and testing before merging. |
| **Developer Experience** | Seamless Git integration (push to deploy), environment variable management, and built-in analytics. |
| **Free Tier** | Generous free tier suitable for small-to-medium projects — no credit card required for basic usage. |

**Alternative:** [Netlify](https://www.netlify.com/) — also excellent for static/Jamstack sites, with a slightly different feature set (split testing, form handling).

---

### Backend Hosting — AWS

The Java/Quarkus backend is best hosted on **AWS Elastic Beanstalk** or **AWS ECS (Fargate)**.

| Service | Use Case | Why |
|---------|----------|-----|
| **Elastic Beanstalk** | Quickest time-to-production | Managed platform with built-in load balancing, auto-scaling, and health monitoring. Deploy with a single CLI command (`eb deploy`). Handles Java app servers (Tomcat, JBoss, or standalone JARs). |
| **ECS Fargate** (Serverless containers) | More control & flexibility | Package the backend as a Docker container and run it on Fargate — a serverless compute engine. No EC2 instances to manage. Integrates naturally with ALB (Application Load Balancer) and CloudWatch. |
| **ECS with EC2** | High throughput / cost optimisation | Use reserved EC2 instances for predictable, high-volume traffic. More hands-on but more cost-effective at scale. |

**Recommended approach (production):**
1. **Dockerise** the backend with a `Dockerfile` (Quarkus produces a thin JAR by default).
2. Deploy to **ECS Fargate** behind an **Application Load Balancer (ALB)**.
3. Use **RDS** (or a managed DB) if persistence is needed in the future.

---

### Security

| Concern | Approach |
|---------|----------|
| **API Protection** | Add **API keys** or **JWT Bearer tokens** for client authentication. The frontend sends the key/token in the `Authorization` header. Quarkus supports JWT via `quarkus-smallrye-jwt`. |
| **Rate Limiting** | Use an API Gateway (e.g., **AWS API Gateway**) in front of the ALB to enforce throttling per client. |
| **Secrets Management** | Store credentials (API keys, DB passwords) in **AWS Secrets Manager** or **Parameter Store**. Never hardcode secrets in source code. Quarkus natively integrates with both. |
| **Network Security** | Deploy the backend in a **private VPC** subnet. Allow inbound traffic only from the ALB (not directly from the internet). Use **Security Groups** to restrict ports (e.g., only 443 from the ALB). |
| **CORS** | Already configured for `http://localhost:3000` in development. In production, restrict CORS to the actual frontend domain (e.g., `https://myapp.vercel.app`). |
| **Dependency Scanning** | Run `./mvnw dependency-check:check` or use **Dependabot** / **Snyk** to automatically detect vulnerable dependencies. |
| **HTTPS** | Terminate TLS at the ALB using **AWS Certificate Manager (ACM)**. All traffic between ALB and backend containers stays within the VPC. |
| **Outbound Access** | The backend only needs outbound HTTPS access to `api.frankfurter.dev`. Restrict outbound rules via **NACLs** or a **NAT Gateway** if the backend is in a private subnet. |

---

### Scalability & Cost

| Aspect | Strategy |
|--------|----------|
| **Horizontal Scaling** | ECS Fargate tasks auto-scale based on CPU/memory metrics or request count via **Application Auto Scaling**. The ALB distributes traffic across healthy tasks. |
| **Vertical Scaling** | Fargate task sizes can be increased (e.g., from 0.5 vCPU / 1 GB RAM to 2 vCPU / 4 GB RAM) for compute-intensive workloads. |
| **Cost Optimisation** | — Use **Fargate Spot** for non-critical workloads (up to 70% discount).<br/>— The Frankfurter API is free, so there are no third-party API costs.<br/>— The frontend on Vercel's free tier handles modest traffic without charge.<br/>— Consider a **single t3.small EC2 instance** via Elastic Beanstalk for very low-traffic deployments (~$15/month). |
| **Caching** | Exchange rates change daily. Cache the Frankfurter API response for the current date to reduce external calls. Quarkus supports Redis / Caffeine caching with `quarkus-cache`. |
| **Read Replicas** | If a database is added later, use **RDS Read Replicas** to offload read traffic. |

---

### Infrastructure as Code (Bonus)

Automate the entire cloud setup using **Terraform** or **AWS CDK**:

```
project-root/
├── terraform/
│   ├── main.tf              # VPC, subnets, security groups
│   ├── ecs.tf               # ECS cluster, task definition, Fargate service
│   ├── alb.tf               # Application Load Balancer + listener + target group
│   ├── route53.tf           # DNS records (e.g., api.myapp.com)
│   ├── acm.tf               # TLS certificate
│   └── outputs.tf           # ALB DNS name, ECR repo URL
├── Dockerfile               # Multi-stage Quarkus container build
└── .github/workflows/
    └── deploy.yml           # CI/CD: build → push to ECR → update ECS service
```

**Alternative — AWS CDK (TypeScript):** If the team is already using TypeScript for the frontend, AWS CDK allows defining infrastructure in familiar syntax. This unifies the frontend, backend, and infra codebases under one language.
