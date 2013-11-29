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
CoalitionGame = require('./CoalitionGame')


if process.argv.length isnt 3
  console.log "Usage: shapley_value input_data"
  process.exit 1


# ########################### CLASS COALITION
class ShapleyCalculator
  constructor: (@game) ->

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
    permutations = this.permutations([1..@game.nb])
    sum = 0
    for permutation in permutations
      inferiors = this.findInferiors(permutation, player)
      v2 = @game.getScore(inferiors)
      inferiors.push(player)
      v1 = @game.getScore(inferiors)
      sum += (v1 - v2);

    return sum / this.fact(@game.nb)

# ########################### PARSE

coalition = null

linereader.eachLine(process.argv[2], (line, last) ->

  if (!coalition)
    coalition = new CoalitionGame parseInt line
  else
    coalition.addScore(line)

  if last
    coalition.printCoalition()
    shapley = new ShapleyCalculator(coalition)
    for x in [1..coalition.nb]
      shpValue = shapley.calculateShapleyValue x
      console.log "Player #{x} has shapley value #{shpValue}"
)





