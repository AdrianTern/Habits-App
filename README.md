# Habits

A full-stack web application for habit tracking, built with Spring Boot and React.

## Tech Stack

- **Backend:** Spring Boot, Java, Maven
- **Frontend:** React (Create React App), JavaScript, HTML, CSS
- **Database:** PostgreSQL (configured via Spring Data JPA)
- **Containerization:** Docker, Docker Compose
- **Build Tools:** Maven
- **Testing:** JUnit (Java), React Testing Library/Jest (JS)
- **Other:** 
  - RESTful API
  - Progressive Web App (PWA) support
  - User authentication and registration
  - Responsive UI

## Main Functionalities

- **User Authentication**
  - Register, login, logout
  - Change password

- **Habit Tracking**
  - Create, update, delete habits/tasks
  - Set maximum password length and task count (configurable)

- **Dashboard**
  - View habits and progress
  - Responsive design for desktop and mobile

- **Notifications**
  - Success/error/info/warning toasts for user feedback

- **RESTful API**
  - Endpoints for user and habit management

- **PWA Features**
  - Manifest and icons for installable experience

- **Deployment**
  - Dockerized for easy deployment
  - Static frontend assets served via Spring Boot

- **Production deployment:** 
   - [https://habits-app-wft7.onrender.com](https://habits-app-wft7.onrender.com) (Render)

## Project Structure

- `Habits/` - Root project folder
  - `src/main/java` - Backend source code
  - `src/main/resources` - Backend resources
  - `src/ui/` - React frontend source
  - `src/ui/build/` - Production build of frontend
  - `target/` - Maven build output
  - `docker-compose.yaml`, `Dockerfile` - Containerization configs
  - `.env` - Environment variables

## Getting Started

1. **Build the backend:**
   ```sh
   ./mvnw clean package
   ```
2. **Build the frontend:**
   ```sh
   cd src/ui
   npm install
   npm run build
   ```
3. **Run with Docker:**
   ```sh
   docker-compose up
   ```
4. **Access the app:**
   - Open [http://localhost:8080](http://localhost:8080) (or configured port)

## Available Scripts (Frontend)

- `npm start` - Run React app in development mode
- `npm test` - Run frontend tests
- `npm run build` - Build frontend for production
- `npm run eject` - Eject Create React App configuration

## Learn More

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Create React App Documentation](https://facebook.github.io/create-react-app/docs/getting-started)
- [Docker Documentation](https://docs.docker.com/)

---

For more details, see [HELP.md](Habits/HELP.md) and [src/ui/README.md](Habits/src/ui/README.md).