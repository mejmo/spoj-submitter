
import sys
from collections import Counter

ranks = ['2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A']
suits = ['S', 'H', 'D', 'C']
order = {'2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9, 'T': 10, 'J': 11, 'Q': 12, 'K': 13, 'A': 14}

class HandChecker:

    def get_suits(self, cards):
        return [card[1] for card in cards]

    def get_ranks(self, cards):
        return [card[0] for card in cards]

    def royal_flush(self, cards):
        cards.sort()
        return True if cards == ['AS', 'JS', 'KS', 'QS', 'TS'] \
                       or cards == ['AH', 'JH', 'KH', 'QH', 'TH'] \
                       or cards == ['AD', 'JD', 'KD', 'QD', 'TD'] \
                       or cards == ['AC', 'JC', 'KC', 'QC', 'TC'] else False

    def straight_flush(self, cards):

        # Check if suits match
        _suits = self.get_suits(cards)
        if _suits != [_suits[0]]*5:
            return False

        # Check if ranks are of a order
        _ranks = self.get_ranks(cards)
        _ranks.sort(key=lambda x: order.get(x))

        if (['2', '3', '4', '5', 'A'] == _ranks):
            return True

        ranks = ['2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A']

        _ranks_template = '|'.join(ranks)
        _ranks_cards = '|'.join(_ranks)

        return True if _ranks_cards in _ranks_template else False

    def four_of_a_kind(self, cards):

        _ranks = self.get_ranks(cards)
        _ranks.sort()

        if _ranks[0] != _ranks[1]:
            _ranks.reverse()

        return True if _ranks.count(_ranks[0]) >= 4 else False

    def full_house(self, cards):

        _ranks = self.get_ranks(cards)
        _ranks.sort()
        _ranks = set(_ranks)

        return True if len(_ranks) <= 2 else False

    def flush(self, cards):

        _suits = self.get_suits(cards)
        _suits = set(_suits)

        return True if len(_suits) == 1 else False

    def straight(self, cards):

        _ranks = self.get_ranks(cards)
        _ranks.sort(key=lambda x: order.get(x))
        ranks.sort(key=lambda x: order.get(x))

        if (['2', '3', '4', '5', 'A'] == _ranks):
            return True

        return '|'.join(_ranks) in '|'.join(ranks)


    def three_of_a_kind(self, cards):

        _ranks = self.get_ranks(cards)
        res = Counter(_ranks)

        return True if 3 in res.values() else False

    def two_pairs(self, cards):

        _ranks = self.get_ranks(cards)
        res = Counter(_ranks)

        return True if 2 in res.values() and len(res.values()) == 3 else False

    def pair(self, cards):

        _ranks = self.get_ranks(cards)
        return True if len(_ranks) > len(set(_ranks)) else False

    def high_card(self, cards):
        return True

    def check_hand(self, cards):

        m = ['royal flush', 'straight flush', 'four of a kind', 'full house', 'flush', 'straight', 'three of a kind', 'two pairs', 'pair', 'high card']

        for method in m:
            if getattr(self, method.replace(' ', '_'))(cards):
                return method

def main():

    s = sys.stdin
    t = int(s.readline())

    for i in xrange(0, t):
        cards = s.readline().split()

        if cards[-1] == "\n":
            cards.pop()

        hand_checker = HandChecker()
        print hand_checker.check_hand(cards)


if __name__ == '__main__':
    main()