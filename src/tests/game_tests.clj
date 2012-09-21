(ns tests.game-tests
  (:use clojure.test
        borg.core
        borg.logic.strategy
        borg.data.parser
        borg.logic.trajectories
        borg.constants
        borg.data.repository
        clojure.contrib.math))

(def game-data-1 [{:time 1348244790110, :left {:y 344.10063065067453, :playerName "Saulis"}, :right {:y 40.6829479057853, :playerName "xx"}, :ball {:pos {:x 413.033371709369, :y 135.996419031424}}}
                  {:time 1348244790260, :left {:y 333.0008943729551, :playerName "Saulis"}, :right {:y 54.6829479057853, :playerName "xx"}, :ball {:pos {:x 356.4251060866989, :y 169.6717361090724}}}
                  {:time 1348244790350, :left {:y 328.56509480188856, :playerName "Saulis"}, :right {:y 64.6829479057853, :playerName "xx"}, :ball {:pos {:x 315.99063064193456, :y 193.7255340216784}}}])

(def game-data-2 [{:time 1348244790800, :left {:y 326.2356869715095, :playerName "Saulis"}, :right {:y 108.6829479057853, :playerName "xx"}, :ball {:pos {:x 138.07893868497138, :y 299.5622448371448}}}
                  {:time 1348244790950, :left {:y 326.3121419596208, :playerName "Saulis"}, :right {:y 122.6829479057853, :playerName "xx"}, :ball {:pos {:x 81.47067306230099, :y 333.2375619147932}}}
                  {:time 1348244791040, :left {:y 326.3400339564209, :playerName "Saulis"}, :right {:y 132.68294790578528, :playerName "xx"}, :ball {:pos {:x 41.036197617536345, :y 357.2913598273992}}}])

(def game-data-3 [{:time 1348244873060, :left {:y 173.46943490827192, :playerName "Saulis"}, :right {:y 225.09352740762398, :playerName "xx"}, :ball {:pos {:x 52.13582775193913, :y 200.52217245350494}}}
                  {:time 1348244873150, :left {:y 165.46943490827192, :playerName "Saulis"}, :right {:y 233.09352740762398, :playerName "xx"}, :ball {:pos {:x 34.34912589904593, :y 177.53094122762195}}}
                  {:time 1348244873240, :left {:y 155.46943490827192, :playerName "Saulis"}, :right {:y 243.09352740762398, :playerName "xx"}, :ball {:pos {:x 12.115748582929427, :y 148.79190219526822}}}])

(def game-data-4 [{:time 1348245272810, :left {:y 143.18688746212808, :playerName "Saulis"}, :right {:y 243.8450249987654, :playerName "xx"}, :ball {:pos {:x 40.96430717024896, :y 145.5257807454184}}}
                  {:time 1348245272960, :left {:y 129.18688746212808, :playerName "Saulis"}, :right {:y 253.62400494653366, :playerName "xx"}, :ball {:pos {:x 10.163955344220312, :y 106.86545103062286}}}
                  {:time 1348245272990, :left {:y 125.18688746212808, :playerName "Saulis"}, :right {:y 256.41728680444197, :playerName "xx"}, :ball {:pos {:x 1.3638548224978422, :y 95.81964254068129}}}])

(def game-data-5 [{:time 1348245601160, :left {:y 187.87646986276076, :playerName "Saulis"}, :right {:y 296.88181351750995, :playerName "xx"}, :ball {:pos {:x 53.86731031484635, :y 144.24432006685856}}}
                  {:time 1348245601250, :left {:y 179.87646986276076, :playerName "Saulis"}, :right {:y 304.6585078256485, :playerName "xx"}, :ball {:pos {:x 33.0339011105508, :y 120.23241291493261}}}
                  {:time 1348245601340, :left {:y 169.87646986276076, :playerName "Saulis"}, :right {:y 314.37937576025513, :playerName "xx"}, :ball {:pos {:x 6.9921396051813645, :y 90.21752897502513}}}])

(defn- difference-between-target-and-landing-heights [data]
  (- (target-height data) (landing-height (previous-ball-position data) (current-ball-position data))))

(deftest game-tests
  (are [x] (> 21 (abs (difference-between-target-and-landing-heights x)))
  game-data-1
  game-data-2
  game-data-3
  game-data-4
  game-data-5
    ))


