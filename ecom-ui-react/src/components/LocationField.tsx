import FieldShell from './FieldShell'

type LocationFieldProps = {
  id: string
  label: string
  value: string
  onChange: (value: string) => void
}

export default function LocationField({ id, label, value, onChange }: LocationFieldProps) {
  return (
    <FieldShell label={label} htmlFor={id}>
      <input
        id={id}
        name={id}
        type="text"
        autoComplete="off"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        placeholder="Enter Address, city, port or terminal"
        className="size-full min-w-0 rounded-[7px] bg-transparent pr-[24px] pl-[20px] text-[12px] text-navy outline-none placeholder:text-navy/50"
      />
    </FieldShell>
  )
}
