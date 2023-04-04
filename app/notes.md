# BUSINESS MODEL

## *TABLES*

### _reservation_
    id: PRIMARY int autoincrement
    user_id: int foreign_key
    court_id: int foreign_key
    players_number: int
    start: date_and_time
    end: date_and_time

### _user_
    id: PRIMARY int autoincrement
    name: String
    surname: String
    birthdate: date
    gender: string
    email: string
    phone_number: string

### _court_
    id: PRIMARY int autoincrement
    sport: int foreign_key
    price_per_hour: real
    address: String

### _sport_
    id: PRIMARY int autoincrement
    name: String

### _reservation_user_
    user_id: PRIMARY foreign_key
    court_id: PRIMARY foreign_key