###
  compute the shapley value of a game
  input file format:
  - first line : number of player
  - then each other line: v  1,2,3=0

  ex:
  2
  1=0
  2=0
  1,2=1

  use star for any player
  2
  *=0
  *,*=1

###

fs = require('fs')
linereader = require('line-reader')


if process.argv.length isnt 3
  console.log "Usage: shapley_value input_data"
  process.exit 1

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
    m = x.match(/([\d\*,]+)\s*=\s*(\d+)/)
    if (m)
      @scores.push new ScoreRule(m[1].split(','), parseInt(m[2]))


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

  fact: (x) ->
    return 1 if x == 1
    return x * this.fact(x-1)

  permutations: (arr, collector=[], ptr = 0) ->
    if (ptr == arr.length)
      collector.push arr
      return
    for i in [ptr...arr.length]
      permutation = arr.concat()
      permutation[ptr] = arr[i]
      permutation[i] = arr[ptr]
      this.permutations(permutation, collector, ptr+1)
    return collector

  findInferiors: (permutation, player) ->
    return permutation.slice(0, permutation.indexOf(player))

  calculateShapleyValue: (player) ->
    permutations = this.permutations([1..@nb])
    sum = 0
    for permutation in permutations
      inferiors = this.findInferiors(permutation, player)
      v2 = this.getScore(inferiors)
      inferiors.push(player)
      v1 = this.getScore(inferiors)
      sum += (v1 - v2);

    return sum / this.fact(@nb)

  printCoalition: () ->
    console.log "Game with #{@nb} players"
    for sc in @scores
      console.log "\t" +sc.toString()

# ########################### PARSE

coalition = null

linereader.eachLine(process.argv[2], (line, last) ->

  if (!coalition)
    coalition = new CoalitionGame parseInt line
  else
    coalition.addScore(line)

  if last
    coalition.printCoalition()
    for x in [1..coalition.nb]
      shpValue = coalition.calculateShapleyValue x
      console.log "Player #{x} has shapley value #{shpValue}"
)





