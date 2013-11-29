###
  Check that a payoff vector is in the core of a coalition game
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


if process.argv.length isnt 4
  console.log "Usage: shapley_value input_data payoff1,payoff2,payoff3"
  process.exit 1


# ########################### COMBINATIONS

combinations = (collector, active, rest) ->
  if rest.length == 0
    collector.push active if active.length isnt 0
  else
    c = rest[0];
    rest = rest.concat();
    rest.splice(0, 1);

    active2 = active.concat();
    active.push(c)
    combinations(collector, active, rest);
    combinations(collector, active2, rest)


# ########################### PARSE

coalition = null

linereader.eachLine(process.argv[2], (line, last) ->

  if (!coalition)
    coalition = new CoalitionGame parseInt line
  else
    coalition.addScore(line)

  if last
    coalition.printCoalition()
    #generate combinations
    coll = []
    combinations(coll, [], [1..coalition.nb])

    #parse payoff
    payoff = []
    for payoffStr in process.argv[3].split(',')
      payoff.push parseFloat payoffStr

    console.log('Core', payoff)
    for comb in coll
      v = 0
      v += payoff[idx-1] for idx in comb

      sc = coalition.getScore(comb)
      console.log('Test ' +sc + ' ' + v + " for ", comb);
      if comb.length == coalition.nb && v != sc
        console.log("Not verified #{v} < #{sc} for", comb)
      else if v < sc
        console.log("Not verified #{v} < #{sc} for", comb)


)





