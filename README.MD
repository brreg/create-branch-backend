# Create Branch Backend

## Usefull information: 
* As currently implemented, the backend application deletes the content in the database, and creates the database model from scratch. Be aware of this when restarting the application.
* Follows the MVC pattern - (model, view, controller)
* Uses websockets for event-pushing from backend to frontend

## Schemas
Schemas are fetched from the [EWC  rulebook and schema repository](https://github.com/EWC-consortium/eudi-wallet-rulebooks-and-schemas/tree/main/data-schemas) and corrected based on a least-change and consistency approach, as the schemas contained in the repository contain errors. 