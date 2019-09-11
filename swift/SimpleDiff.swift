import Foundation

class SimpleDiff {

  enum DiffType<T> {
    case insert([T])
    case delete([T])
    case equal([T])
  }

  func diff<T>(_ old: [T], with new: [T]) -> [DiffType<T>] where T: Hashable {

    // Create a map from old values to their indices
    var oldIndexMap = [T: [Int]]()
    for (i, val) in old.enumerated() {
      var indexes = oldIndexMap[val] ?? []
      indexes.append(i)
      oldIndexMap[val] = indexes
    }

    var subStartOld = 0
    var subStartNew = 0
    var subLength = 0

    var overlap = [Int: Int]()
    for (iNew, val) in new.enumerated() {
      for iOld in oldIndexMap[val] ?? [] {
        let _subLength = (overlap[iOld - 1] ?? 0) + 1
        if _subLength > subLength {
          subLength = _subLength
          subStartOld = iOld - subLength + 1
          subStartNew = iNew - subLength + 1
        }
      }
    }

    if subLength == 0 {
      return (old.count > 0 ? [.delete(old)] : []) + (new.count > 0 ? [.insert(new)] : [])
    } else {
      var _diffs:[DiffType<T>] = []
      _diffs += diff(Array(old[0..<subStartOld]), with: Array(new[0..<subStartNew]))
      _diffs.append(.equal(Array(new[subStartNew..<subStartNew+subLength])))
      _diffs += diff(Array(old[subStartOld+subLength..<old.count]), with: Array(new[subStartNew+subLength..<new.count]))
      return _diffs
    }
  }
}