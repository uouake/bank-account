export class Operations {
  id: number
  operationDate: Date
  amount: number
  typeOp: string

  constructor(id: number, operationDate: Date, amount: number, typeOp: string) {
    this.id = id
    this.operationDate = operationDate
    this.amount = amount
    this.typeOp = typeOp
  }
}
