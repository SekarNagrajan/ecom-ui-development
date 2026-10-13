import type { ReactNode } from 'react'

type FieldShellProps = {
  label: string
  htmlFor: string
  children: ReactNode
}

/** Label + 42px tinted field container shared by the search form inputs. */
export default function FieldShell({ label, htmlFor, children }: FieldShellProps) {
  return (
    <div className="flex min-w-0 flex-1 flex-col justify-center gap-[8px]">
      <label htmlFor={htmlFor} className="text-[12px] whitespace-nowrap text-navy">
        {label}
      </label>
      <div className="relative flex h-[42px] w-full items-center rounded-[7px] bg-navy/10 focus-within:ring-2 focus-within:ring-brand/50">
        {children}
      </div>
    </div>
  )
}
