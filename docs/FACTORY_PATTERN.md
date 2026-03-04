# Factory Pattern

## Applicability & Reasoning
- Context: the system creates multiple aircraft variants (`PassengerPlane`, `Helicopter`, `PassengerDrone`) in setup flows (for example in `Runner`).
- Problem addressed: direct instantiation spreads creation rules across callers and couples client code to concrete classes.
- Why Factory: `AircraftFactory` centralizes creation decisions and returns the abstraction (`Aircraft`) so callers depend on a stable API.
- Introducing `AircraftFactory` as a strategic class addition to encapsulate object creation and support cleaner architecture with polymorphism.

## Before & After Code Snippets
**Before (distributed direct object creation in client code)**
```java
static List<Aircraft> aircrafts = Arrays.asList(
    new PassengerPlane("A380"),
    new PassengerPlane("A350"),
    new PassengerPlane("Embraer 190"),
    new PassengerPlane("Antonov AN2"),
    new Helicopter("H1"),
    new PassengerDrone("HypaHype")
);
```

**After (centralized creation through factory method)**
```java
public class AircraftFactory {
    public static Aircraft createAircraft(String type, String model) {
        switch (type) {
            case "Passenger Plane":
                return new PassengerPlane(model);
            case "Helicopter":
                return new Helicopter(model);
            case "Passenger Drone":
                return new PassengerDrone(model);
            default:
                throw new IllegalArgumentException(String.format("Aircraft type '%s' is not recognized", type));
        }
    }
}
```

## Benefits
- Centralized object creation logic in one place (`AircraftFactory`).
- Reduced coupling: clients use `Aircraft` instead of concrete constructors.
- Easier evolution: constructor/signature changes are handled in factory, not every caller.
- Better consistency: type-to-class mapping is standardized.
- Cleaner setup code in clients like `Runner`.

## Drawbacks & Mitigations
- Adding a new aircraft type still requires updating the factory `switch`; mitigate with tests that assert all supported types.
- String-based type input can cause runtime errors; mitigate with constants/enums if needed.
- One factory method can grow over time; mitigate by splitting factories by domain if complexity increases.

## class diagrams

<div style="display:flex;gap:10px;align-items:flex-start;">
    <div style="width:48%;text-align:center;">
        <div style="margin-top:6px;font-weight:600;">Before</div>
        <img src="before_factory_pattern.jpeg" alt="before-factory" style="width:100%;height:auto;object-fit:contain;" />
    </div>
    <div style="width:48%;text-align:center;">
        <div style="margin-bottom:6px;font-weight:600;">After</div>
        <img src="after_factory_pattern.jpeg" alt="after-factory" style="width:100%;height:auto;object-fit:contain;" />
    </div>
</div>

## Code Changes
Factory extraction moved aircraft construction into one class and replaced distributed constructor calls with factory calls.

- `AircraftFactory.java`:

```java
public class AircraftFactory {
    public static Aircraft createAircraft(String type, String model) {
        switch (type) {
            case "Passenger Plane":
                return new PassengerPlane(model);
            case "Helicopter":
                return new Helicopter(model);
            case "Passenger Drone":
                return new PassengerDrone(model);
            default:
                throw new IllegalArgumentException(
                    String.format("Aircraft type '%s' is not recognized", type)
                );
        }
    }
}
```

- Example usage from initialization code:

```java
static List<Aircraft> aircrafts = Arrays.asList(
    AircraftFactory.createAircraft("Passenger Plane", "A380"),
    AircraftFactory.createAircraft("Passenger Plane", "A350"),
    AircraftFactory.createAircraft("Passenger Plane", "Embraer 190"),
    AircraftFactory.createAircraft("Passenger Plane", "Antonov AN2"),
    AircraftFactory.createAircraft("Helicopter", "H1"),
    AircraftFactory.createAircraft("Passenger Drone", "HypaHype")
);
```