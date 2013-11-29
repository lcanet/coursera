linereader = require('line-reader')

# ########################### CLASS SCORE

class ScoreRule
  constructor: (@players, @value) ->

  isCompatible: (otherPlayers ) ->
    if otherPlayers.length != @players.length
      return false
    for i in [0...@players.length]
      # the first time i'll be glad to use implicit type casting in js
      if (@players[i] != '*' && ('' + @players[i] != '' + otherPlayers[i]))
        return false
    return true

  toString: () ->
    'v(' + @players + ') = ' + @value


# ########################### CLASS COALITION
class CoalitionGame
  constructor: (@nb) ->
    @scores = []

  addScore: (x) ->
    m = x.match(/([\d\*,]+)\s*=\s*(\d+.?\d*)/)
    if (m)
      @scores.push new ScoreRule(m[1].split(','), parseFloat(m[2]))


  findMatchingScore: (players) ->
    for sc in @scores
      if sc.isCompatible(players)
        return sc
    return null

  getScore: (players) ->
    if players.length == 0
      return 0

    # try with given permutation
    players.sort()
    matching = this.findMatchingScore(players)

    return matching.value if matching isnt null

    console.log("WARN: cannot find a scoring for coalition set ", players);
    return 0

  printCoalition: () ->
    console.log "Game with #{@nb} players"
    for sc in @scores
      console.log "\t" +sc.toString()


module.exports = CoalitionGame
