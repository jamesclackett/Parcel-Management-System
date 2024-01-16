# Parcel Management System

---

Inspired by need to track inbound/outbound student parcels being routed through a
convenience store beside a large university. A particular courier was denied access to the
campus and had to use the store as a parcel drop-off point for 9 months. This meant an
influx of approximately 10 parcels each day.

An application was needed to monitor the parcels that had entered the store and their details (name, address, company, color). 
The store complained that many students would never pick up their parcels, resulting in a huge backlog. Due to storage space constraints, old and new parcels often mixed. 
This made finding new parcels very difficult.

This system is able to visualise all the parcels that are currentlty ready for collection, those which have expired (been in shop for 30+ days), a history of 
collected parcels, and an entire history of inbound/collected parcels.

---

## Architecture

The application makes use of an embedded H2 Database and a MYSQL connector. As the convenience store is small, there was no need to create a networked database solution. 
One device can be used to track parcels.
It is written in Java and uses Swing for its UI.


To try out this application there are two options:
* Clone the project and run it.
* Run the production JAR file 'ParcelManagementSystem.jar'

---

## User Interface

#### 'Ready' tab and 'enter details' box:
<img width="1194" alt="image" src="https://github.com/jamesclackett/Parcel-Management-System/assets/55019466/ded6aae4-4a94-4fda-80da-927ff39f0594">

#### 'Expired' tab:
<img width="1194" alt="image" src="https://github.com/jamesclackett/Parcel-Management-System/assets/55019466/be42d742-3ada-492c-af53-742ef02a0717">

#### 'All' tab:
<img width="1194" alt="image" src="https://github.com/jamesclackett/Parcel-Management-System/assets/55019466/c8dc2a57-1015-478f-bb32-42ea31d73f24">


