class SimpleGame
  constructor: (@nbActions1=2, @nbActions2=2) ->
    @payoffs = [];
    for i in [0...nbActions1]
      @payoffs[i] = [];
      for j in [0...nbActions2]
        @payoffs[i].push [0, 0]

  setPayoff: (player1Action, player2Action, payoff) ->
    @payoffs[player1Action][player2Action] = payoff

  setPayoffs: (payoffs) ->
    @payoffs = payoffs

  getPayoff: (player1Action, player2Action) ->
    return @payoffs[player1Action][player2Action]


  isZeroSum:  ->
    isZeroSumPayoff = (payoff) ->
      return payoff[0] + payoff[1] == 0
    for i in [0...@nbActions1]
      for j in [0...@nbActions2]
        return false if !isZeroSumPayoff @payoffs[i][j]
    return true

  isNashEquilibrium: (action1, action2) ->
    eqPayoff = this.getPayoff(action1, action2)

    for i in [0...@nbActions1]
      payoff = this.getPayoff(i, action2)
      return false if (payoff[0] > eqPayoff[0])
    for j in [0...@nbActions2]
      payoff = this.getPayoff(action1, j)
      return false if (payoff[1] > eqPayoff[1])
    return true

  getPureStrategyNashEquilibriums: ->
    equilibriums = []
    for i in [0...@nbActions1]
      for j in [0...@nbActions2]
        if (this.isNashEquilibrium(i,j))
          equilibriums.push([i, j])
    return equilibriums

  isDominatedPlayer1Strategy: (action, strict=true) ->
    for i in [0...@nbActions1]
      if (i != action)
        for j in [0...@nbActions2]
          p1 = this.getPayoff(i ,j)
          p2 = this.getPayoff(action ,j)
          return false if (p1[0] < p2[0])
          return false if (strict && p1[0] <= p2[0])
    return true

  isDominatedPlayer2Strategy: (action, strict=true) ->
    for j in [0...@nbActions2]
      if (j != action)
        for i in [0...@nbActions1]
          p1 = this.getPayoff(i ,j)
          p2 = this.getPayoff(i, action)
          return false if (p1[1] < p2[1])
          return false if (strict && p1[1] <= p2[1])
    return true

  getDominatedStrategies: (strict=true) ->
    strategies = []
    domStrategies = []
    for i in [0...@nbActions1]
      domStrategies.push i if this.isDominatedPlayer1Strategy(i, strict)
    strategies.push domStrategies
    domStrategies = []
    for j in [0...@nbActions2]
      domStrategies.push j if this.isDominatedPlayer2Strategy(j, strict)
    strategies.push domStrategies
    return strategies

  isParetoOptimal: (x, y) ->
    payoff = this.getPayoff(x, y)
    for i in [0...@nbActions1]
      for j in [0...@nbActions2]
        otherPayoff = this.getPayoff(i ,j );
        return false if payoff[0] < otherPayoff[0] && payoff[1] < otherPayoff[1]
    return true

  getParetoOptimalOutcomes: ->
    outcomes = []
    for i in [0...@nbActions1]
      for j in [0...@nbActions2]
        outcomes.push [i, j] if this.isParetoOptimal(i,j)
    return outcomes


module?.exports = SimpleGame
window?.SimpleGame = SimpleGame