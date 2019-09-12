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

    let overlap = [Int: Int]()
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


extension SimpleDiff.DiffType:CustomStringConvertible where T == String {

  var description: String {
    switch self {
    case .insert(let values):
      return "+\(values.joined(separator: ""))"
    case .delete(let values):
      return "-\(values.joined(separator: ""))"
    case .equal(let values):
      return "=\(values.joined(separator: ""))"
    }
  }
}

let simpleDiff = SimpleDiff()
let old = "hello world!"
let new = "hi world?"
print("OLD:")
print(old)
print("\nNEW:")
print(new)

let result = simpleDiff.diff(old.map { "\($0)" }, with: new.map { "\($0)" })
  .reduce("", { (result, acc) -> String in
  return result + acc.description
})

assert(result == "=h-ello+i= =w=o=r=l=d-!+?")
print("\nRESULT:")
print(result)