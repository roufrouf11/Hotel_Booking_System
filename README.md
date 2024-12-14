# Hotel Booking System

## Overview
The Hotel Booking System is a client-server application designed to facilitate hotel room reservations. It features a server handling hotel and room data, and a client allowing users to search, view, and reserve rooms based on preferences like location, price, and date.

## Features
### Client:
- **Interactive Menus**: Options include:
  1. Main Menu
  2. Location Menu
  3. Price Menu
  4. Package Menu
  5. Date Menu
- **Socket Communication**: Sends user input to the server for processing.

### Server:
- **Threaded Client Handling**: Uses a `ClientHandler` class implementing `Runnable` to handle multiple client requests simultaneously.
- **Hotel and Room Management**: Stores hotel and room data, processes reservations, and ensures data integrity.
- **File-Based Data Storage**: Reads and writes hotel and room data to a `db.txt` file for persistence.

### Room Reservations:
- Matches user preferences (e.g., location, price range, number of beds) with available rooms.
- Checks reservation conflicts in real-time to prevent double booking.

### Core Classes:
1. **Client**:
   - Sends user choices to the server.
   - Displays results received from the server.

2. **Server**:
   - Handles incoming connections and processes user requests.
   - Manages hotel and room data.

3. **Room**:
   - Tracks reservation dates.
   - Provides dynamic pricing based on the season.

4. **Hotel**:
   - Maintains a list of rooms.

5. **DateHandler**:
   - Converts between string and `Date` formats.
   - Determines the season for dynamic pricing.

## System Architecture
### Client:
- Initiates connection using a socket.
- Sends user input as a formatted string.
- Receives and displays server responses.

### Server:
1. Accepts client connections via a `ServerSocket`.
2. Processes requests in a separate thread for each client.
3. Matches user inputs with room availability and returns results.
4. Updates `db.txt` for persistent data storage.

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/your-repository.git
   cd your-repository
   ```
2. Compile and run the server:
   ```bash
   javac Server.java
   java Server
   ```
3. Compile and run the client:
   ```bash
   javac Client.java
   java Client
   ```

## Usage
- **Start the Server**: Launch the server to initialize hotel data and listen for client connections.
- **Run the Client**: Connect to the server and interact using the provided menus.

## Example Workflow
1. User selects a location and enters price and date preferences.
2. Server processes the request and sends a list of matching rooms.
3. User selects a room to reserve.
4. Server verifies availability, confirms the reservation, and updates the database.

## File Structure
- `Server.java`: Contains server-side logic, including client handling.
- `Client.java`: Contains client-side logic for user interaction.
- `Room.java`: Represents room details and pricing logic.
- `Hotel.java`: Manages a list of rooms and their availability.
- `DateHandler.java`: Handles date formatting and seasonal pricing logic.
- `db.txt`: Stores hotel and room data for persistence.

