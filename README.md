# Scenic Spokes

Scenic Spokes is a full-stack web application designed for motorcycle enthusiasts who believe the ride is the destination. Whether you’re heading to the legendary Sturgis Motorcycle Rally or planning a local weekend adventure, Scenic Spokes helps you generate thrilling, scenic routes using the TomTom API, connect with community-hosted events, and make the most out of every mile.

## Technologies Used

### Frontend
- React
- React Router
- Leaflet (for interactive maps)
- Axios
- Google Places API 
- Clerk (for authentication)
- React Toastify (for alerts)

### Backend
- Spring Boot
- Java 
- Spring Security 
- Hibernate + JPA
- MySQL
- TomTom Routing API

## Installation & Setup

### Prerequisites
- Node.js and npm
- Java 21
- MySQL (with a `scenic_spokes` database created)
- Clerk account (for authentication)
- TomTom Developer account (for API key)

### Backend Setup (Spring Boot)

1. **Clone the repository**  

git clone https://github.com/lsmith1580/unit-2-final-project-lindsey-smith.git
cd scenic-spokes-backend

2. **Configure environment variables**  
Create an `application.properties` file:

spring.datasource.url=jdbc:mysql://localhost:3306/scenic_spokes
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.security.oauth2.resourceserver.jwt.issuer-uri=https://your-clerk-instance.clerk.accounts.dev

tomtom.api.key=your_tomtom_api_key

3. **Run the backend**
   
./mvnw spring-boot:run

###Frontend Setup (React + Vite)

1. **Navigate to the frontend directory**

cd scenic-spokes-frontend

2. **Install dependencies**

npm install

3. **Create a .env file**

VITE_GOOGLE_API_KEY=your_google_maps_api_key
VITE_CLERK_PUBLISHABLE_KEY=your_clerk_publishable_key
VITE_BACKEND_URL=http://localhost:8080

4. **Start the frontend**

npm run dev

## Links

### Wireframe

https://miro.com/app/board/uXjVIzJXSbA=/?share_link_id=450465184892

### Entity Relationship Diagram

https://www.figma.com/board/Y7dF4uvN19IQPmqjDj2gIG/ER-Diagram---Scenic-Spokes?node-id=0-1&t=ebIKvsEMSKiLjzsC-1

##Unsolved Problems / Future Features

-Route Saving & Sharing: Currently, routes are not saved to user profiles. A future enhancement could include saving and sharing generated routes.

-User Profile Pages: The app supports authentication but does not yet have a page for user profile.

-Event Filtering: Adding date/location filters and sorting features to the events list.

-Route Customization Options: Letting users customize route preferences like duration, terrain, or scenic stops.
