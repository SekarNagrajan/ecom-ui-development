import FieldShell from './FieldShell'
import Icon from './Icon'

const MONTHS = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']

/** Formats an ISO date (YYYY-MM-DD) as "25 Jan, 2027". */
function formatDate(iso: string) {
  const [year, month, day] = iso.split('-')
  if (!year || !month || !day) return ''
  return `${day} ${MONTHS[Number(month) - 1]}, ${year}`
}

type DateFieldProps = {
  id: string
  label: string
  value: string
  min?: string
  /** Shown faded (as in the design) until the user picks a date. */
  isDefault: boolean
  onChange: (value: string) => void
}

export default function DateField({ id, label, value, min, isDefault, onChange }: DateFieldProps) {
  return (
    <FieldShell label={label} htmlFor={id}>
      <div className="pointer-events-none flex size-full items-center justify-between px-[20px]">
        <span className={`text-[12px] whitespace-nowrap text-navy ${isDefault ? 'opacity-50' : ''}`}>
          {formatDate(value)}
        </span>
        <Icon name="calendar" />
      </div>
      {/* Native picker sits invisibly over the field so it stays keyboard and screen-reader accessible. */}
      <input
        id={id}
        name={id}
        type="date"
        value={value}
        min={min}
        required
        onChange={(event) => onChange(event.target.value)}
        onClick={(event) => {
          try {
            event.currentTarget.showPicker()
          } catch {
            // showPicker is unsupported in some browsers; the native control still works.
          }
        }}
        className="absolute inset-0 size-full cursor-pointer opacity-0"
      />
    </FieldShell>
  )
}
