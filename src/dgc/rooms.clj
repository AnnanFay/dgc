(ns .rooms
  "")

; A room is a set of tiles to be placed within a specific boundary
; TODO: How are doors chosen? Also room rotation?
(defmacro defroom)

;A layout is a specific layout of rooms and tiles
(defmacro deflayout)

(defroom bedroom
	3 3
	[bed cabinet container])

(defroom office
	3 3
	[chair table])

; l l l l l
; l       l
; l       l
; l       l
; l l l l l
(defroom lever-room
	5 5
	[16 lever])

(deflayout airlock)

; Command Centre Module
; In: 		Food
; Out: 		Commands
; Access: Mechanic

; Provides housing and food for 1 dwarf
; Lever room with levers
; access for mechanic to build

(deflayout command-centre
	[bedroom lever-room])

()