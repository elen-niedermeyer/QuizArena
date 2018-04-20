package com.quizarena.sessions

class ParticipantsUtils {

    companion object {

        /**
         * Adds the ranking for each participant of the list.
         * There could be a ranking like: 1, 1, 3, ...
         *
         * @param participants the list of participants where to add the ranking
         * @return the list sorted and with ranks
         */
        fun addRanking(participants: List<Participant>): List<Participant> {
            val participantsSorted = sortListByScore(participants)

            var rank = 1;
            var score = participantsSorted.get(0).sessionScore

            participantsSorted.forEachIndexed { index, participant ->
                if (participant.sessionScore < score) {
                    score = participant.sessionScore
                    rank = index + 1
                }
                participant.ranking = rank
            }

            return participantsSorted
        }

        /**
         * Sorts the list of participants by their score. Sorting descending.
         *
         * @param list list of participants to sort
         * @return the sorted list
         */
        private fun sortListByScore(list: List<Participant>): List<Participant> {
            return list.sortedWith(compareBy { -it.sessionScore })
        }
    }
}