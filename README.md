# Tiffany's

Java Chess Libraries

## Artifacts

- tiffanys-commons (Bassic chess components like rules, game, board, PGN and FEN)
- tiffanys-chesslink (ChessLink / Chess Genius Exclusive protocol implementation)
- tiffanys-lichess (Lichess API implementation)
- tiffanys-dragonborn (Chess engine, including UCI support)
- tiffanys-ui (Multiplatform UI including ChessLink, Lichess & Dragonborn support)


## Multiplatform UI

Download Tiffany's UI for Mac & Windows [here](https://github.com/bvongunten/tiffanys/releases/tag/v0.6.0-alpha)

Tiffany's UI supports two playing modes of which both support the usage of a ChessLink board: 

- Player vs. Engine / Player vs. Player 
- Player vs. Lichess Opponent

### Chesslink Support

Tiffany's supports currently playing via ChessLink on USB.

- Mac OSX: The software should find your board right away.
- Windows: You might have to install USB drivers for the board. Find information [here](https://www.hiarcs.com/eboard/ChessLinkDrivers.htm) or [here](https://zadig.akeo.ie/).

### Lichess Support

- You have to create an API Token on your lichess account page: [https://lichess.org/account/oauth/token](https://lichess.org/account/oauth/token)  
- Paste your username and the token in the options dialog of Tiffanys-UI
- You have initiate / accept the game on your regular Lichess Client (Mobile/Browser)
- Select any of your open games in the Lichess mode of Tiffany's UI and play your game ;)

Following video shows the usage of the Chess Genius Exclusive Board vs. an Opponent from Lichess. [Youtube Video](https://www.youtube.com/watch?v=ZexqLLzHAd4)
