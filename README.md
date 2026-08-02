# Project-Software-Architecture-Design---Gruppo-10

## Membri del Gruppo 10

- Alfonso Villani (`VxVilla55`)
- Giovanni Lamberti (`itsjhell`)
- Luca Lanzetta (`Gea1926`)
- Nicola Liguori (`nicolaliguori1`)

## Link

- Trello: https://trello.com/invite/b/6a0dc846f6a9c7feae2676e0/ATTI73a200419530ff1fc943f0f8c2ec83eeB012C290/progetto-software-architecture-design-gruppo-10

- Google Documents: https://docs.google.com/document/d/1Oe3vhcPqP5QNY0Wi5qX-AQkWQugsR5DuseXLVN1jydk/edit?usp=sharing

- Diagrams.net: https://drive.google.com/file/d/1_bj8-N31Ai44VPcHy-LVDjGUKfPTFNzc/view?usp=sharing

### Albero del progetto
```text
PROJECT-SOFTWARE-ARCHITECTURE-DESIGN--GRUPPO-10
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com\group10
│   │   │       ├── controller
│   │   │       │   ├── common
│   │   │       │   │   └── AbstractUIComponent.java
│   │   │       │   ├── playlist
│   │   │       │   │   ├── PlaylistUIAdderController.java
│   │   │       │   │   ├── PlaylistUIComponentCard.java
│   │   │       │   │   ├── PlaylistUIComponentItem.java
│   │   │       │   │   ├── PlaylistUIDetailsController.java
│   │   │       │   │   └── PlaylistUIOptionsController.java
│   │   │       │   ├── track
│   │   │       │   │   ├── AddToPlaylistController.java
│   │   │       │   │   ├── TrackUIAdderController.java
│   │   │       │   │   ├── TrackUIComponentCard.java
│   │   │       │   │   ├── TrackUIComponentItem.java
│   │   │       │   │   ├── TrackUIDetailsController.java
│   │   │       │   │   ├── TrackUIOptionsController.java
│   │   │       │   │   └── TrackUIQueueComponentItem.java
│   │   │       │   ├── HomepageController.java
│   │   │       │   ├── MainViewController.java
│   │   │       │   ├── PlayerViewController.java
│   │   │       │   └── QueueViewController.java
│   │   │       ├── model
│   │   │       │   ├── builder
│   │   │       │   │   ├── PlaylistBuilder.java
│   │   │       │   │   └── TrackBuilder.java
│   │   │       │   ├── common
│   │   │       │   │   ├── Builder.java
│   │   │       │   │   ├── Playable.java
│   │   │       │   │   ├── Publisher.java
│   │   │       │   │   └── Subscriber.java
│   │   │       │   ├── persistence
│   │   │       │   │   ├── CatalogueFile.java
│   │   │       │   │   ├── JsonPersistenceManager.java
│   │   │       │   │   ├── PersistenceManager.java
│   │   │       │   │   ├── PlaylistData.java
│   │   │       │   │   └── TrackData.java
│   │   │       │   ├── playback
│   │   │       │   │   ├── PlaybackMode.java
│   │   │       │   │   ├── RepeatPlaylist.java
│   │   │       │   │   ├── RepeatTrack.java
│   │   │       │   │   └── Sequential.java
│   │   │       │   ├── state
│   │   │       │   │   ├── PausedState.java
│   │   │       │   │   ├── PlaybackEngine.java
│   │   │       │   │   ├── PlayerState.java
│   │   │       │   │   ├── PlayingState.java
│   │   │       │   │   └── StoppedState.java
│   │   │       │   ├── MusicCatalogue.java
│   │   │       │   ├── PlaylistComponent.java
│   │   │       │   └── TrackComponent.java
│   │   │       ├── service
│   │   │       │   ├── command
│   │   │       │   │   ├── AddPlaylistCommand.java
│   │   │       │   │   ├── AddTagCommand.java
│   │   │       │   │   ├── AddTrackCommand.java
│   │   │       │   │   ├── AddTrackToPlaylistCommand.java
│   │   │       │   │   ├── Command.java
│   │   │       │   │   ├── CommandManager.java
│   │   │       │   │   ├── DeletePlaylistCommand.java
│   │   │       │   │   ├── DeleteTrackCommand.java
│   │   │       │   │   ├── RemoveTagCommand.java
│   │   │       │   │   ├── RemoveTrackFromPlaylistCommand.java
│   │   │       │   │   ├── RenamePlaylistCommand.java
│   │   │       │   │   ├── ReorderTrackCommand.java
│   │   │       │   │   └── UpdateTrackCommand.java
│   │   │       │   ├── factory
│   │   │       │   │   ├── PlaylistUIComponentFactory.java
│   │   │       │   │   ├── TrackUIComponentFactory.java
│   │   │       │   │   └── UIComponentFactory.java
│   │   │       │   └── filter
│   │   │       │       ├── GenreFilterStrategy.java
│   │   │       │       ├── TagFilterStrategy.java
│   │   │       │       ├── TrackFilterStrategy.java
│   │   │       │       └── YearFilterStrategy.java
│   │   │       ├── App.java
│   │   │       └── module-info.java
│   │   └── resources
│   └── test
├── target
├── .gitignore
├── nbactions.xml
├── pom.xml
└── README.md
```
